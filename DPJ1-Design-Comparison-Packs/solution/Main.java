import java.util.Objects;

public class Main {
    public enum Shipping { PICKUP, COURIER, OVERNIGHT }

    private static void validUnits(int units) {
        if (units < 0 || units > 1_000_000) {
            throw new IllegalArgumentException("units must be 0..1000000");
        }
    }
    public static int grossCents(int units) {
        validUnits(units);
        return units * (units >= 10 ? 250 : units >= 5 ? 300 : 350);
    }
    private static int memberCents(int gross) { return gross - gross / 10; }
    private static int courierFee(int units) { return units == 0 ? 0 : 500 + 20 * units; }
    public static int plainTotalCents(int units, boolean member, Shipping shipping) {
        int gross = grossCents(units);
        Objects.requireNonNull(shipping, "shipping");
        int fee = switch (shipping) {
            case PICKUP -> 0;
            case COURIER -> courierFee(units);
            case OVERNIGHT -> units == 0 ? 0 : 1500 + 30 * units;
        };
        return (member ? memberCents(gross) : gross) + fee;
    }

    public abstract static class LegacyOrder {
        protected final int units;
        protected LegacyOrder(int units) { validUnits(units); this.units = units; }
        public abstract int totalCents();
    }
    public static final class RegularPickup extends LegacyOrder {
        public RegularPickup(int units) { super(units); }
        public int totalCents() { return grossCents(units); }
    }
    public static final class RegularCourier extends LegacyOrder {
        public RegularCourier(int units) { super(units); }
        public int totalCents() { return grossCents(units) + courierFee(units); }
    }
    public static final class MemberPickup extends LegacyOrder {
        public MemberPickup(int units) { super(units); }
        public int totalCents() { return memberCents(grossCents(units)); }
    }
    public static final class MemberCourier extends LegacyOrder {
        public MemberCourier(int units) { super(units); }
        public int totalCents() { return memberCents(grossCents(units)) + courierFee(units); }
    }

    public interface PricingPolicy { int subtotalCents(int units); }
    public interface ShippingPolicy { int feeCents(int units); }
    public static final class RegularPricing implements PricingPolicy {
        public int subtotalCents(int units) { return grossCents(units); }
    }
    public static final class MemberPricing implements PricingPolicy {
        public int subtotalCents(int units) { return memberCents(grossCents(units)); }
    }
    public static final class PickupShipping implements ShippingPolicy {
        public int feeCents(int units) { validUnits(units); return 0; }
    }
    public static final class CourierShipping implements ShippingPolicy {
        public int feeCents(int units) { validUnits(units); return courierFee(units); }
    }
    public static final class OvernightShipping implements ShippingPolicy {
        public int feeCents(int units) {
            validUnits(units);
            return units == 0 ? 0 : 1500 + 30 * units;
        }
    }
    public static final class Checkout {
        private final PricingPolicy pricing;
        private final ShippingPolicy shipping;
        public Checkout(PricingPolicy pricing, ShippingPolicy shipping) {
            this.pricing = Objects.requireNonNull(pricing, "pricing");
            this.shipping = Objects.requireNonNull(shipping, "shipping");
        }
        public int totalCents(int units) {
            validUnits(units);
            return pricing.subtotalCents(units) + shipping.feeCents(units);
        }
    }
    public static String displayQuote(Checkout checkout, int units) {
        return "Quote: " + checkout.totalCents(units);
    }
    public static int invoiceTotal(Checkout checkout, int units) {
        return checkout.totalCents(units);
    }
    public static int simpleMinutes(int hours) {
        return Math.multiplyExact(hours, 60);
    }
    public interface MinutesPolicy { int convert(int hours); }
    public static final class FixedMinutes implements MinutesPolicy {
        public int convert(int hours) { return simpleMinutes(hours); }
    }
    public static final class MinutesFactory {
        public MinutesPolicy create() { return new FixedMinutes(); }
    }
    public static int factoryMinutes(int hours) {
        return new MinutesFactory().create().convert(hours);
    }
    public static int inconsistentInvoiceCents(int units) {
        validUnits(units);
        return units * 350;
    }

    public static void main(String[] args) {
        System.out.println(new Checkout(new RegularPricing(), new PickupShipping()).totalCents(6));
        System.out.println(new Checkout(new RegularPricing(), new CourierShipping()).totalCents(6));
        System.out.println(new Checkout(new MemberPricing(), new PickupShipping()).totalCents(6));
        System.out.println(new Checkout(new MemberPricing(), new CourierShipping()).totalCents(6));
    }
}
