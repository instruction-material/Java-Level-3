package tickets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/** Working candidate: move state-specific transitions to TicketState. */
public final class Ticket implements TicketAPI {
    private String status = "DRAFT";
    private final List<String> history = new ArrayList<>();
    public boolean apply(String action) {
        Objects.requireNonNull(action, "action");
        if (!List.of("approve", "finish", "reset").contains(action))
            throw new IllegalArgumentException("action");
        String next = switch (status) {
            case "DRAFT" -> action.equals("approve") ? "READY" : status;
            case "READY" -> action.equals("finish") ? "DONE" : status;
            case "DONE" -> action.equals("reset") ? "DRAFT" : status;
            default -> throw new IllegalStateException("status");
        };
        if (next.equals(status)) return false;
        history.add(status + "->" + next);
        status = next;
        return true;
    }
    public String status() { return status; }
    public List<String> history() { return List.copyOf(history); }
}
