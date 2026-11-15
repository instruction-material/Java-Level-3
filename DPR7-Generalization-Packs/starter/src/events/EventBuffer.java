package events;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Replace list inheritance: callers can currently bypass the domain operations. */
public class EventBuffer extends ArrayList<String> implements EventBufferAPI {
    private static final long serialVersionUID = 1L;
    private final int capacity;
    public EventBuffer(int capacity) {
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
