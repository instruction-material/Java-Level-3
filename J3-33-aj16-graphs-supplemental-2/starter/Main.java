import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    static List<String> breadthFirst(Map<String, List<String>> graph, String start) {
        // TODO: Visit reachable vertices in breadth-first order.
        throw new UnsupportedOperationException("Visit reachable vertices in breadth-first order");
    }

    public static void main(String[] args) {
        var graph = Map.of("A", List.of("B", "C"), "B", List.of("D"), "C", List.of("A", "D"));
        System.out.println(breadthFirst(graph, "A"));
    }
}
