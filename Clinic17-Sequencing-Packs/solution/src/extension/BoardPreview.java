package extension;
import api.*;
import java.util.*;
public final class BoardPreview {
    private BoardPreview() {}
    public static List<Task> sorted(List<Task> tasks) {
        List<Task> result=new ArrayList<>(BoardRows.copy(tasks));
        result.sort(Comparator.comparing(Task::title));
        return List.copyOf(result);
    }
}
