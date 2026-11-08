import java.util.Objects;
import pricing.CartPricer;
import pricing.Plan;
import pricing.legacy.LegacyPricer;

public final class ContractTests {
    private static int checks;
    private interface Api {
        long quote(Plan plan, int units);
        String receipt(Plan plan, int units);
    }
    private static void equal(Object actual, Object expected) {
        checks++;
        if (!Objects.equals(actual, expected))
            throw new AssertionError("Expected " + expected + ", got " + actual);
    }
    private static void rejects(Class<? extends Throwable> expected, Runnable task) {
        checks++;
        try { task.run(); }
        catch (Throwable error) {
            if (expected.isInstance(error)) return;
            throw new AssertionError("Wrong failure type", error);
        }
        throw new AssertionError("Expected " + expected.getName());
    }
    // Independent interval data rather than the application's rule dispatch.
    private static long oracle(Plan plan, int units) {
        if (plan == Plan.FLAT) return 400L * units;
        long[][] bands = {{0, 4, 350}, {5, 9, 300}, {10, 1000, 250}};
        for (long[] band : bands) {
            if (units >= band[0] && units <= band[1]) {
                long cents = band[2] * units;
                return plan == Plan.MEMBER ? cents * 9 / 10 : cents;
            }
        }
        throw new AssertionError("Oracle domain");
    }
    private static void core(Api api) {
        for (Plan plan : Plan.values()) {
            for (int units = 0; units <= 1000; units++) {
                long expected = oracle(plan, units);
                equal(api.quote(plan, units), expected);
                equal(api.receipt(plan, units), plan.name() + "|" + units + "|" + expected);
            }
            for (int invalid : new int[] {-1, 1001}) {
                rejects(IllegalArgumentException.class, () -> api.quote(plan, invalid));
                rejects(IllegalArgumentException.class, () -> api.receipt(plan, invalid));
            }
        }
        rejects(NullPointerException.class, () -> api.quote(null, 0));
        rejects(NullPointerException.class, () -> api.receipt(null, 0));
        rejects(NullPointerException.class, () -> api.quote(null, -1));
        equal(api.quote(Plan.BULK, 4), 1400L);
        equal(api.quote(Plan.BULK, 5), 1500L);
        equal(api.quote(Plan.BULK, 6), 1800L);
        equal(api.quote(Plan.BULK, 9), 2700L);
        equal(api.quote(Plan.BULK, 10), 2500L);
        equal(api.quote(Plan.FLAT, 6), 2400L);
        equal(api.quote(Plan.MEMBER, 6), 1620L);
    }
    private static void optional(CartPricer cart) {
        int[] calls = {0};
        equal(cart.priceUsing(6, units -> { calls[0]++; equal(units, 6); return units * 275L; }), 1650L);
        equal(calls[0], 1);
        equal(cart.priceUsing(0, units -> 0), 0L);
        equal(cart.priceUsing(1000, units -> Long.MAX_VALUE), Long.MAX_VALUE);
        rejects(IllegalArgumentException.class, () -> cart.priceUsing(-1, units -> 0));
        rejects(IllegalArgumentException.class, () -> cart.priceUsing(1001, units -> 0));
        rejects(NullPointerException.class, () -> cart.priceUsing(0, null));
        rejects(IllegalArgumentException.class, () -> cart.priceUsing(6, units -> -1));
        RuntimeException original = new IllegalStateException("rule failure");
        try {
            cart.priceUsing(6, units -> { throw original; });
            throw new AssertionError("Rule failure was lost");
        } catch (RuntimeException actual) { equal(actual, original); }
        equal(cart.quote(Plan.MEMBER, 6), 1620L);
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!mode.equals("base") && !mode.equals("core") && !mode.equals("all"))
            throw new IllegalArgumentException("mode");
        LegacyPricer legacy = new LegacyPricer();
        core(new Api() {
            public long quote(Plan plan, int units) { return legacy.quote(plan, units); }
            public String receipt(Plan plan, int units) { return legacy.receipt(plan, units); }
        });
        if (!mode.equals("base")) {
            CartPricer cart = new CartPricer();
            core(new Api() {
                public long quote(Plan plan, int units) { return cart.quote(plan, units); }
                public String receipt(Plan plan, int units) { return cart.receipt(plan, units); }
            });
            if (mode.equals("all")) optional(cart);
        }
        System.out.println((mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core" : "Complete")
            + " strategy studio contracts: " + checks + " passed");
    }
}
