import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var graph = java.util.Map.of("A", List.of("C", "B", "B"), "C", List.of("D"), "B", List.of("D", "B"),
                "Elsewhere", List.<String>of());
        expect(List.of("A", "C", "B", "D"), Main.breadthFirst(graph, "A"));
        expect(List.of("D"), Main.breadthFirst(java.util.Map.of("D", List.of()), "D"));
        expect(List.of(), Main.breadthFirst(graph, "unknown"));
        expect(List.of("C", "B", "B"), graph.get("A"));

        System.out.println("Graph checks passed");
    }
}
