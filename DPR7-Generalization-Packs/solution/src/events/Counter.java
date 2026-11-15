package events;
/** Complete parent contract: nonnegative, checked addition, read and reset. */
public class Counter implements CounterAPI {
    private long value;
    public final void add(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount");
        value = Math.addExact(value, amount);
    }
    public final long value() { return value; }
    public final void reset() { value = 0; }
}
