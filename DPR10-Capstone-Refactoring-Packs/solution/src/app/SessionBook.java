package app;
import api.*;
import java.util.*;
final class SessionBook {
    private final String id;
    private final int capacity;
    private boolean open = true;
    private final Set<String> enrolled = new LinkedHashSet<>();
    private final List<String> waiting = new ArrayList<>();
    private final List<String> history = new ArrayList<>(List.of("open"));
    SessionBook(String id, int capacity) { this.id = id; this.capacity = capacity; }
    JoinStatus join(String participant) {
        requireOpen();
        if (enrolled.contains(participant) || waiting.contains(participant)) return JoinStatus.DUPLICATE;
        if (enrolled.size() < capacity) {
            enrolled.add(participant);
            history.add("join:"+participant+":enrolled");
            return JoinStatus.ENROLLED;
        }
        waiting.add(participant);
        history.add("join:"+participant+":waiting");
        return JoinStatus.WAITLISTED;
    }
    boolean leave(String participant) {
        requireOpen();
        if (waiting.remove(participant)) {
            history.add("leave:"+participant+":waiting");
            return true;
        }
        if (!enrolled.remove(participant)) return false;
        history.add("leave:"+participant+":enrolled");
        if (!waiting.isEmpty()) {
            String next = waiting.remove(0);
            enrolled.add(next);
            history.add("promote:"+next);
        }
        return true;
    }
    boolean close() {
        if (!open) return false;
        open = false;
        history.add("close");
        return true;
    }
    SessionView view() { return new SessionView(id, capacity, open, new ArrayList<>(enrolled), waiting, history); }
    private void requireOpen() { if (!open) throw new IllegalStateException("closed"); }
}
