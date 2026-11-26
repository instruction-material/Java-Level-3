import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    static List<String> breadthFirst(Map<String, List<String>> graph, String start) {
        if (!graph.containsKey(start)) return List.of();
        var queue = new ArrayDeque<String>();
        var seen = new HashSet<String>();
        var order = new ArrayList<String>();
        queue.addLast(start);
        seen.add(start);
        while (!queue.isEmpty()) {
            String next = queue.removeFirst();
            order.add(next);
            for (String neighbor : graph.getOrDefault(next, List.of())) {
                if (seen.add(neighbor)) queue.addLast(neighbor);
            }
        }
        return order;
    }

    public static void main(String[] args) {
        var graph = Map.of("A", List.of("B", "C"), "B", List.of("D"), "C", List.of("A", "D"));
        System.out.println(breadthFirst(graph, "A"));
    }
}
