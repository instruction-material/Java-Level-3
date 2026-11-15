package legacy;
import api.*;
import java.util.*;
/** Working before design: parallel maps and mixed registration/report responsibilities. */
public final class LegacyWorkshopDesk implements DeskAPI {
    private final Map<String,Integer> capacities = new LinkedHashMap<>();
    private final Map<String,Boolean> opened = new LinkedHashMap<>();
    private final Map<String,LinkedHashSet<String>> enrolled = new LinkedHashMap<>();
    private final Map<String,List<String>> waiting = new LinkedHashMap<>();
    private final Map<String,List<String>> histories = new LinkedHashMap<>();
    public LegacyWorkshopDesk() {}
    public void open(String session, int capacity) {
        Rules.id(session, "session");
        Rules.capacity(capacity);
        if (capacities.containsKey(session)) throw new IllegalStateException("session");
        capacities.put(session, capacity);
        opened.put(session, true);
        enrolled.put(session, new LinkedHashSet<>());
        waiting.put(session, new ArrayList<>());
        histories.put(session, new ArrayList<>(List.of("open")));
    }
    public JoinStatus join(String session, String participant) {
        Rules.id(session, "session");
        Rules.id(participant, "participant");
        if (!capacities.containsKey(session)) throw new NoSuchElementException("session");
        if (!opened.get(session)) throw new IllegalStateException("closed");
        if (enrolled.get(session).contains(participant) || waiting.get(session).contains(participant))
            return JoinStatus.DUPLICATE;
        if (enrolled.get(session).size() < capacities.get(session)) {
            enrolled.get(session).add(participant);
            histories.get(session).add("join:"+participant+":enrolled");
            return JoinStatus.ENROLLED;
        }
        waiting.get(session).add(participant);
        histories.get(session).add("join:"+participant+":waiting");
        return JoinStatus.WAITLISTED;
    }
    public boolean leave(String session, String participant) {
        Rules.id(session, "session");
        Rules.id(participant, "participant");
        if (!capacities.containsKey(session)) throw new NoSuchElementException("session");
        if (!opened.get(session)) throw new IllegalStateException("closed");
        if (waiting.get(session).contains(participant)) {
            waiting.get(session).remove(participant);
            histories.get(session).add("leave:"+participant+":waiting");
            return true;
        }
        if (enrolled.get(session).contains(participant)) {
            enrolled.get(session).remove(participant);
            histories.get(session).add("leave:"+participant+":enrolled");
            if (!waiting.get(session).isEmpty()) {
                String next = waiting.get(session).remove(0);
                enrolled.get(session).add(next);
                histories.get(session).add("promote:"+next);
            }
            return true;
        }
        return false;
    }
    public boolean close(String session) {
        Rules.id(session, "session");
        if (!capacities.containsKey(session)) throw new NoSuchElementException("session");
        if (!opened.get(session)) return false;
        opened.put(session, false);
        histories.get(session).add("close");
        return true;
    }
    public SessionView view(String session) {
        Rules.id(session, "session");
        if (!capacities.containsKey(session)) throw new NoSuchElementException("session");
        return new SessionView(session, capacities.get(session), opened.get(session),
            new ArrayList<>(enrolled.get(session)), waiting.get(session), histories.get(session));
    }
    public List<SessionView> all() {
        List<SessionView> result = new ArrayList<>();
        for (String id : capacities.keySet())
            result.add(new SessionView(id, capacities.get(id), opened.get(id),
                new ArrayList<>(enrolled.get(id)), waiting.get(id), histories.get(id)));
        return List.copyOf(result);
    }
    public String report(String style) {
        Rules.style(style);
        StringBuilder text = new StringBuilder();
        if (style.equals("plain")) {
            text.append("Workshops ").append(capacities.size()).append('\n');
            for (String id : capacities.keySet()) {
                text.append(id).append(" capacity=").append(capacities.get(id))
                    .append(" open=").append(opened.get(id)).append('\n');
                text.append("enrolled=").append(enrolled.get(id).isEmpty() ? "-" : String.join(",", enrolled.get(id))).append('\n');
                text.append("waiting=").append(waiting.get(id).isEmpty() ? "-" : String.join(",", waiting.get(id))).append('\n');
            }
        } else {
            text.append(capacities.size()).append('\n');
            for (String id : capacities.keySet()) {
                text.append(id).append('|').append(capacities.get(id)).append('|')
                    .append(opened.get(id) ? "open" : "closed").append('|')
                    .append(enrolled.get(id).isEmpty() ? "-" : String.join(",", enrolled.get(id))).append('|')
                    .append(waiting.get(id).isEmpty() ? "-" : String.join(",", waiting.get(id))).append('\n');
            }
        }
        return text.toString();
    }
}
