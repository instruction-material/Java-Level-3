package boundary;

/** Immutable enum instance versus an explicitly constructed mutable dependency. */
public final class LifetimeExamples {
    private LifetimeExamples() {}
    public enum CurrencySettings {
        INSTANCE;
        public String currency() { return "USD"; }
    }
    public static final class Counter {
        private int value;
        public int next() { value = Math.addExact(value, 1); return value; }
    }
}
