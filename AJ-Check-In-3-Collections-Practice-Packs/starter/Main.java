import java.util.ArrayDeque;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static List<String> reverse(List<String> names) {
        // TODO: Reverse the queue through a stack.
        throw new UnsupportedOperationException("Reverse the queue through a stack");
    }

    static List<String> alphabetize(List<String> names) {
        // TODO: Rebuild the queue through a priority queue.
        throw new UnsupportedOperationException("Rebuild the queue through a priority queue");
    }

    public static void main(String[] args) {
        var names = List.of("Cora", "Ada", "Bea", "Ada");
        System.out.println(names);
        var reversed = reverse(names);
        System.out.println(reversed);
        System.out.println(alphabetize(reversed));
    }
}
