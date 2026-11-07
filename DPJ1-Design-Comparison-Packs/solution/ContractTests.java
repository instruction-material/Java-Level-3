public class ContractTests {
    private static int checks;
    private static void equal(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Wrong failure", error);
            checks++;
            return;
        }
        throw new AssertionError("Missing " + type.getSimpleName());
    }
    private static int oracle(int units, boolean member, Main.Shipping kind) {
        int price = units * (units < 5 ? 350 : units < 10 ? 300 : 250);
        if (member) price -= price / 10;
        int fee = units == 0 ? 0 : kind == Main.Shipping.PICKUP ? 0 :
            kind == Main.Shipping.COURIER ? 500 + 20 * units : 1500 + 30 * units;
        return price + fee;
    }
    private static void check(int units, boolean member, Main.Shipping kind) {
        int expected = oracle(units, member, kind);
        Main.PricingPolicy pricing = member ? new Main.MemberPricing() : new Main.RegularPricing();
        Main.ShippingPolicy shipping = switch (kind) {
            case PICKUP -> new Main.PickupShipping();
            case COURIER -> new Main.CourierShipping();
            case OVERNIGHT -> new Main.OvernightShipping();
        };
        Main.Checkout checkout = new Main.Checkout(pricing, shipping);
        equal(checkout.totalCents(units), expected);
        equal(Main.plainTotalCents(units, member, kind), expected);
        equal(Main.invoiceTotal(checkout, units), expected);
        if (!Main.displayQuote(checkout, units).equals("Quote: " + expected)) throw new AssertionError("Display client changed");
        checks++;
        if (kind != Main.Shipping.OVERNIGHT) {
            Main.LegacyOrder legacy = member ?
                (kind == Main.Shipping.PICKUP ? new Main.MemberPickup(units) : new Main.MemberCourier(units)) :
                (kind == Main.Shipping.PICKUP ? new Main.RegularPickup(units) : new Main.RegularCourier(units));
            equal(legacy.totalCents(), expected);
        }
    }
    public static void main(String[] args) {
        boolean baseOnly = args.length == 1 && args[0].equals("base");
        Main.Shipping[] modes = baseOnly ?
            new Main.Shipping[]{Main.Shipping.PICKUP, Main.Shipping.COURIER} : Main.Shipping.values();
        for (int units = 0; units <= 1000; units++)
            for (boolean member : new boolean[]{false, true})
                for (Main.Shipping mode : modes) check(units, member, mode);
        for (boolean member : new boolean[]{false, true})
            for (Main.Shipping mode : modes) check(1_000_000, member, mode);
        fails(IllegalArgumentException.class, () -> Main.grossCents(-1));
        fails(IllegalArgumentException.class, () -> Main.grossCents(1_000_001));
        fails(IllegalArgumentException.class, () -> new Main.RegularPickup(-1));
        fails(IllegalArgumentException.class, () -> new Main.MemberCourier(1_000_001));
        fails(IllegalArgumentException.class, () -> new Main.Checkout(new Main.RegularPricing(), new Main.PickupShipping()).totalCents(-1));
        fails(NullPointerException.class, () -> new Main.Checkout(null, new Main.PickupShipping()));
        fails(NullPointerException.class, () -> new Main.Checkout(new Main.RegularPricing(), null));
        fails(NullPointerException.class, () -> Main.plainTotalCents(1, false, null));
        equal(Main.simpleMinutes(2), 120);
        equal(Main.factoryMinutes(2), 120);
        equal(Main.grossCents(5), 1500);
        equal(Main.inconsistentInvoiceCents(5), 1750);
        if (!baseOnly) {
            fails(IllegalArgumentException.class, () -> new Main.OvernightShipping().feeCents(-1));
            fails(IllegalArgumentException.class, () -> new Main.OvernightShipping().feeCents(1_000_001));
        }
        System.out.println((baseOnly ? "Base design contracts: " : "Complete design contracts: ") + checks + " passed");
    }
}
