package api;
import java.util.List;
import java.util.Objects;
public record SessionView(String id, int capacity, boolean open,
                          List<String> enrolled, List<String> waiting, List<String> history) {
    public SessionView {
        Rules.id(id, "session");
        Rules.capacity(capacity);
        enrolled = List.copyOf(Objects.requireNonNull(enrolled, "enrolled"));
        waiting = List.copyOf(Objects.requireNonNull(waiting, "waiting"));
        history = List.copyOf(Objects.requireNonNull(history, "history"));
    }
}
