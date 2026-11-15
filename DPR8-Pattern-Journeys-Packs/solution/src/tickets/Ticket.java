package tickets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public final class Ticket implements TicketAPI {
    private TicketState state = TicketState.DRAFT;
    private final List<String> history = new ArrayList<>();
    public boolean apply(String action) {
        Objects.requireNonNull(action, "action");
        if (!List.of("approve", "finish", "reset").contains(action))
            throw new IllegalArgumentException("action");
        TicketState next = state.next(action);
        if (next == state) return false;
        history.add(state.name() + "->" + next.name());
        state = next;
        return true;
    }
    public String status() { return state.name(); }
    public List<String> history() { return List.copyOf(history); }
}
