package tickets;
import java.util.List;
public interface TicketAPI {
    boolean apply(String action);
    String status();
    List<String> history();
}
