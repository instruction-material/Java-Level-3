import java.util.Arrays;

public class Main {
    record Task(String label, int priority) {}

    static Task[] order(Task[] input) {
        Task[] result = input.clone();
        for (int next = 1; next < result.length; next++) {
            Task saved = result[next];
            int position = next;
            while (position > 0 && result[position - 1].priority() > saved.priority()) {
                result[position] = result[position - 1];
                position--;
            }
            result[position] = saved;
        }
        return result;
    }

    public static void main(String[] args) {
        Task[] tasks = {new Task("upload", 2), new Task("review", 1), new Task("archive", 2)};
        System.out.println(Arrays.stream(order(tasks)).map(Task::label).toList());
    }
}
