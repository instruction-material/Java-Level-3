package tickets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/** A selectable policy is Strategy; the engine, not the policy, owns current state. */
public final class StrategyTicket implements TicketAPI {
    @FunctionalInterface public interface TransitionPolicy { String next(String state, String action); }
    private final TransitionPolicy policy;
    private String status = "DRAFT";
    private final List<String> history = new ArrayList<>();
    public StrategyTicket(TransitionPolicy policy) { this.policy = Objects.requireNonNull(policy, "policy"); }
    public static StrategyTicket standard() {
        return new StrategyTicket((state, action) -> {
            if (state.equals("DRAFT") && action.equals("approve")) return "READY";
            if (state.equals("READY") && action.equals("finish")) return "DONE";
            if (state.equals("DONE") && action.equals("reset")) return "DRAFT";
            return state;
        });
    }
    public boolean apply(String action) {
        Objects.requireNonNull(action, "action");
        if (!List.of("approve", "finish", "reset").contains(action)) throw new IllegalArgumentException("action");
        String next = policy.next(status, action);
        if (!List.of("DRAFT", "READY", "DONE").contains(next)) throw new IllegalStateException("policy");
        if (next.equals(status)) return false;
        history.add(status + "->" + next); status = next; return true;
    }
    public String status() { return status; }
    public List<String> history() { return List.copyOf(history); }
}
