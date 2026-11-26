import java.util.Arrays;

public class Main {
    record Task(String label, int priority) {}

    static Task[] order(Task[] input) {
        // TODO: Insert tasks by ascending priority and preserve ties.
        throw new UnsupportedOperationException("Insert tasks by ascending priority and preserve ties");
    }

    public static void main(String[] args) {
        Task[] tasks = {new Task("upload", 2), new Task("review", 1), new Task("archive", 2)};
        System.out.println(Arrays.stream(order(tasks)).map(Task::label).toList());
    }
}
