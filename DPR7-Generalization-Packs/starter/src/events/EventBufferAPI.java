package events;
import java.util.List;
public interface EventBufferAPI {
    void push(String event);
    String next();
    List<String> snapshot();
}
