package api;
import java.util.*;
public final class BoardRows {
    private BoardRows() {}
    public static List<Task> copy(List<Task> tasks) {
        Objects.requireNonNull(tasks,"tasks");
        if(tasks.size()>8)throw new IllegalArgumentException("tasks");
        List<Task> input=new ArrayList<>(tasks);
        for(Task task:input)Objects.requireNonNull(task,"task");
        return List.copyOf(input);
    }
}
