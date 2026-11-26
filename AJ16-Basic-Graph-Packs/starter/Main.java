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
        // TODO: Register an isolated vertex without replacing its existing neighbors.
        throw new UnsupportedOperationException("Register an isolated vertex without replacing its existing neighbors");
    }

    static boolean addEdge(Graph graph, String from, String to) {
        // TODO: Store one ordered edge and count it once.
        throw new UnsupportedOperationException("Store one ordered edge and count it once");
    }

    static List<String> neighbors(Graph graph, String vertex) {
        // TODO: Return a read-only snapshot of outgoing neighbors.
        throw new UnsupportedOperationException("Return a read-only snapshot of outgoing neighbors");
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
