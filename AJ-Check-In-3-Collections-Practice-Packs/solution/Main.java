import java.util.ArrayDeque;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static List<String> reverse(List<String> names) {
        var queue = new ArrayDeque<>(names);
        var stack = new ArrayDeque<String>();
        while (!queue.isEmpty()) stack.push(queue.removeFirst());
        while (!stack.isEmpty()) queue.addLast(stack.pop());
        return List.copyOf(queue);
    }

    static List<String> alphabetize(List<String> names) {
        var ordered = new PriorityQueue<>(names);
        var queue = new ArrayDeque<String>();
        while (!ordered.isEmpty()) queue.addLast(ordered.remove());
        return List.copyOf(queue);
    }

    public static void main(String[] args) {
        var names = List.of("Cora", "Ada", "Bea", "Ada");
        System.out.println(names);
        var reversed = reverse(names);
        System.out.println(reversed);
        System.out.println(alphabetize(reversed));
    }
}
