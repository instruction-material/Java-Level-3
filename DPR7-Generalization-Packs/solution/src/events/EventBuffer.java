package events;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
public final class EventBuffer implements EventBufferAPI {
    private final int capacity;
    private final Deque<String> events = new ArrayDeque<>();
    public EventBuffer(int capacity) {
        if (capacity < 1 || capacity > 8) throw new IllegalArgumentException("capacity");
        this.capacity = capacity;
    }
    public void push(String event) {
        Objects.requireNonNull(event, "event");
        if (event.isBlank()) throw new IllegalArgumentException("event");
        if (events.size() == capacity) events.removeFirst();
        events.addLast(event);
    }
    public String next() {
        if (events.isEmpty()) throw new NoSuchElementException("empty");
        return events.removeFirst();
    }
    public List<String> snapshot() { return List.copyOf(events); }
}
