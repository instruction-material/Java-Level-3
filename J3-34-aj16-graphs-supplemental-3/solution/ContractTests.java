import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var a = new Main.Arc("A", "B");
        var b = new Main.Arc("B", "B");
        var c = new Main.Arc("B", "C");
        var actual = Main.degrees(List.of("Z", "A"), List.of(a, a, b, c));
        expect(List.of("A", "B", "C", "Z"), List.copyOf(actual.keySet()));
        expect(new Main.Degree(0, 1), actual.get("A"));
        expect(new Main.Degree(2, 2), actual.get("B"));
        expect(new Main.Degree(1, 0), actual.get("C"));
        expect(new Main.Degree(0, 0), actual.get("Z"));
        expect(java.util.Map.of(), Main.degrees(List.of(), List.of()));

        System.out.println("Graph checks passed");
    }
}
