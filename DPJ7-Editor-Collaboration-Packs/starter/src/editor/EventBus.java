// Observer: registration identity and a membership snapshot define dispatch lifetime.
package editor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class EventBus {
    @FunctionalInterface
    public interface Subscription extends AutoCloseable {
        @Override void close();
    }
    private static final class Entry {
        private final Consumer<EditEvent> listener;
        private Entry(Consumer<EditEvent> listener) { this.listener = listener; }
    }
    private final List<Entry> entries = new ArrayList<>();
    public int size() { return entries.size(); }
    public Subscription subscribe(Consumer<EditEvent> listener) {
        Entry entry = new Entry(Objects.requireNonNull(listener, "listener"));
        entries.add(entry);
        return () -> entries.remove(entry);
    }
    public void publish(EditEvent event) {
        throw new UnsupportedOperationException("Implement captured-list event dispatch.");
    }
    public Subscription subscribeOnce(Consumer<EditEvent> listener) {
        throw new UnsupportedOperationException("Implement optional once-only subscription.");
    }
}
