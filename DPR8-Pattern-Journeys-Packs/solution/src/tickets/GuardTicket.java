package tickets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/** Complete smaller alternative for this small, fixed lifecycle. */
public final class GuardTicket implements TicketAPI {
    private String status = "DRAFT";
    private final List<String> history = new ArrayList<>();
    public boolean apply(String action) {
        Objects.requireNonNull(action, "action");
        if (!List.of("approve", "finish", "reset").contains(action))
            throw new IllegalArgumentException("action");
        if (status.equals("DRAFT") && action.equals("approve")) return move("READY");
        if (status.equals("READY") && action.equals("finish")) return move("DONE");
        if (status.equals("DONE") && action.equals("reset")) return move("DRAFT");
        return false;
    }
    private boolean move(String next) { history.add(status + "->" + next); status = next; return true; }
    public String status() { return status; }
    public List<String> history() { return List.copyOf(history); }
}
