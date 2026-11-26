import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException correct) {
            return;
        }
        throw new AssertionError("Expected an invalid input to be rejected");
    }

    public static void main(String[] args) {
        var weights = java.util.Map.of(new Main.Arc("A", "B"), 0L, new Main.Arc("B", "C"), 5L,
                new Main.Arc("A", "C"), 5L, new Main.Arc("A", "D"), Long.MAX_VALUE, new Main.Arc("D", "C"), 9L);
        expect(new Main.Choice(5, List.of("A", "B", "C")),
                Main.best(weights, List.of(List.of("A", "D", "C"), List.of("A", "B", "C"), List.of("A", "C"))).orElseThrow());
        expect(new Main.Choice(Long.MAX_VALUE, List.of("A", "D")), Main.best(weights, List.of(List.of("A", "D"))).orElseThrow());
        expect(java.util.Optional.empty(), Main.best(weights, List.of(List.of("C", "A"), List.of())));
        expect(new Main.Choice(0, List.of("A")), Main.best(weights, List.of(List.of("A"))).orElseThrow());
        var path = new java.util.ArrayList<>(List.of("A", "C"));
        var saved = Main.best(weights, List.of(path)).orElseThrow();
        path.clear();
        expect(List.of("A", "C"), saved.route());

        System.out.println("Advanced structures checks passed");
    }
}
