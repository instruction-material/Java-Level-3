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
        Objects.requireNonNull(event, "event");
        for (Entry entry : List.copyOf(entries)) entry.listener.accept(event);
    }
    public Subscription subscribeOnce(Consumer<EditEvent> listener) {
        Objects.requireNonNull(listener, "listener");
        boolean[] fired = {false};
        Subscription[] own = new Subscription[1];
        own[0] = subscribe(event -> {
            if (fired[0]) return;
            fired[0] = true;
            own[0].close();
            listener.accept(event);
        });
        return own[0];
    }
}
