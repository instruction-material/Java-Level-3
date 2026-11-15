package app;
import api.*;
import java.util.*;
/** Public facade keeps catalog order; each book owns one session's mutable state. */
public final class WorkshopDesk implements DeskAPI {
    private final Map<String,SessionBook> sessions = new LinkedHashMap<>();
    public WorkshopDesk() {}
    public void open(String session, int capacity) {
        Rules.id(session, "session");
        Rules.capacity(capacity);
        if (sessions.containsKey(session)) throw new IllegalStateException("session");
        sessions.put(session, new SessionBook(session, capacity));
    }
    public JoinStatus join(String session, String participant) {
        Rules.id(session, "session"); Rules.id(participant, "participant");
        return book(session).join(participant);
    }
    public boolean leave(String session, String participant) {
        Rules.id(session, "session"); Rules.id(participant, "participant");
        return book(session).leave(participant);
    }
    public boolean close(String session) {
        Rules.id(session, "session"); return book(session).close();
    }
    public SessionView view(String session) {
        Rules.id(session, "session"); return book(session).view();
    }
    public List<SessionView> all() {
        List<SessionView> result = new ArrayList<>();
        for (SessionBook session : sessions.values()) result.add(session.view());
        return List.copyOf(result);
    }
    public String report(String style) {
        Rules.style(style);
        return DeskReports.render(all(), style);
    }
    private SessionBook book(String session) {
        SessionBook result = sessions.get(session);
        if (result == null) throw new NoSuchElementException("session");
        return result;
    }
}
