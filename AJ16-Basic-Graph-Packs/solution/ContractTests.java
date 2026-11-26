import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var graph = new Main.Graph();
        Main.addVertex(graph, "Isolated");
        expect(true, Main.addEdge(graph, "A", "B"));
        expect(false, Main.addEdge(graph, "A", "B"));
        expect(List.of(), Main.neighbors(graph, "B"));
        var saved = Main.neighbors(graph, "A");
        expect(true, Main.addEdge(graph, "B", "A"));
        expect(true, Main.addEdge(graph, "A", "A"));
        expect(false, Main.addEdge(graph, "A", "A"));
        expect(List.of("B"), saved);
        expect(List.of("B", "A"), Main.neighbors(graph, "A"));
        expect(List.of(), Main.neighbors(graph, "Isolated"));
        expect(List.of(), Main.neighbors(graph, "Missing"));
        expect(3, graph.outgoing.size());
        expect(3, graph.edges);
        try {
            Main.addEdge(graph, "New", null);
            throw new AssertionError("Accepted a null endpoint");
        } catch (NullPointerException expected) {
            expect(false, graph.outgoing.containsKey("New"));
        }

        System.out.println("Graph checks passed");
    }
}
