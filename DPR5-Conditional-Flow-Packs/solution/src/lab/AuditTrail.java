package lab;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lab.BookingData.Decision;

/** Caller-owned observable work. Keep the order and number of probes. */
public final class AuditTrail {
    private final List<String> events = new ArrayList<>();
    public void note(String event) { events.add(Objects.requireNonNull(event)); }
    public boolean training(int actual, int required) {
        events.add("training:" + actual + ">=" + required);
        return actual >= required;
    }
    public boolean available(boolean busy) {
        events.add("availability:busy=" + busy);
        return !busy;
    }
    public void decision(Decision value) { events.add("decision:" + value.reason()); }
    public List<String> events() { return List.copyOf(events); }
}
