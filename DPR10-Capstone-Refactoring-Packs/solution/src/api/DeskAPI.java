package api;
import java.util.List;
public interface DeskAPI {
    void open(String session, int capacity);
    JoinStatus join(String session, String participant);
    boolean leave(String session, String participant);
    boolean close(String session);
    SessionView view(String session);
    List<SessionView> all();
    String report(String style);
}
