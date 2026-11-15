package api;
import java.util.List;
public interface BoardAPI {
    String summary(List<Task> tasks);
    int remainingMinutes(List<Task> tasks);
}
