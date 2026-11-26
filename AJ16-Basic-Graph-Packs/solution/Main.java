import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    static final class Graph {
        final Map<String, LinkedHashSet<String>> outgoing = new LinkedHashMap<>();
        int edges;
    }

    static void addVertex(Graph graph, String vertex) {
        graph.outgoing.computeIfAbsent(Objects.requireNonNull(vertex), ignored -> new LinkedHashSet<>());
    }

    static boolean addEdge(Graph graph, String from, String to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        addVertex(graph, from);
        addVertex(graph, to);
        boolean added = graph.outgoing.get(from).add(to);
        if (added) graph.edges++;
        return added;
    }

    static List<String> neighbors(Graph graph, String vertex) {
        var outgoing = graph.outgoing.get(vertex);
        return outgoing == null ? List.of() : List.copyOf(outgoing);
    }

    public static void main(String[] args) {
        var graph = new Graph();
        addVertex(graph, "Pier");
        addEdge(graph, "Library", "Park");
        addEdge(graph, "Park", "Library");
        addEdge(graph, "Park", "Museum");
        addEdge(graph, "Park", "Museum");
        System.out.println(neighbors(graph, "Park"));
        System.out.println(neighbors(graph, "Pier"));
        System.out.println(graph.edges);
    }
}
