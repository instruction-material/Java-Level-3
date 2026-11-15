package events;
import java.util.Objects;
/** All Counter operations are forwarded unchanged; assess a genuine subtype. */
public final class NamedCounter implements NamedValue {
    private final Counter counter = new Counter();
    private final String label;
    public NamedCounter(String label) {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        this.label = label;
    }
    public void add(long amount) { counter.add(amount); }
    public long value() { return counter.value(); }
    public void reset() { counter.reset(); }
    public String label() { return label; }
}
