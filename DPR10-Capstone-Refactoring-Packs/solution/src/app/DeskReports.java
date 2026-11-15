package app;
import api.SessionView;
import java.util.List;
final class DeskReports {
    private DeskReports() {}
    static String render(List<SessionView> sessions, String style) {
        StringBuilder text = new StringBuilder();
        if (style.equals("plain")) {
            text.append("Workshops ").append(sessions.size()).append('\n');
            for (SessionView session : sessions) {
                text.append(session.id()).append(" capacity=").append(session.capacity())
                    .append(" open=").append(session.open()).append('\n');
                text.append("enrolled=").append(row(session.enrolled())).append('\n');
                text.append("waiting=").append(row(session.waiting())).append('\n');
            }
        } else {
            text.append(sessions.size()).append('\n');
            for (SessionView session : sessions)
                text.append(session.id()).append('|').append(session.capacity()).append('|')
                    .append(session.open() ? "open" : "closed").append('|')
                    .append(row(session.enrolled())).append('|').append(row(session.waiting())).append('\n');
        }
        return text.toString();
    }
    private static String row(List<String> values) { return values.isEmpty() ? "-" : String.join(",", values); }
}
