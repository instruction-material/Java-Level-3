package events;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Frozen before designs for the three independent relationship decisions. */
public final class LegacyEventModels {
    private LegacyEventModels() {}
    private static class Buffer extends ArrayList<String> implements EventBufferAPI {
        private static final long serialVersionUID = 1L;
        private final int capacity;
        Buffer(int capacity) {
            if (capacity < 1 || capacity > 8) throw new IllegalArgumentException("capacity");
            this.capacity = capacity;
        }
        public void push(String event) {
            Objects.requireNonNull(event, "event");
            if (event.isBlank()) throw new IllegalArgumentException("event");
            if (size() == capacity) remove(0);
            add(event);
        }
        public String next() {
            if (isEmpty()) throw new NoSuchElementException("empty");
            return remove(0);
        }
        public List<String> snapshot() { return List.copyOf(this); }
    }
    private static final class DefaultBuffer extends Buffer {
        private static final long serialVersionUID = 1L;
        DefaultBuffer(int capacity) { super(capacity); }
    }
    private static final class Named implements NamedValue {
        private final Counter counter = new Counter();
        private final String label;
        Named(String label) {
            Objects.requireNonNull(label, "label");
            if (label.isBlank()) throw new IllegalArgumentException("label");
            this.label = label;
        }
        public void add(long amount) { counter.add(amount); }
        public long value() { return counter.value(); }
        public void reset() { counter.reset(); }
        public String label() { return label; }
    }
    public static EventBufferAPI buffer(int capacity) { return new DefaultBuffer(capacity); }
    public static NamedValue named(String label) { return new Named(label); }
}
