import java.util.List;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }
    public static void main(String[] args) {
        same(List.of(new Main.Stop("A", 0)), Main.report("A", List.of()));
        same(List.of(new Main.Stop("A", 0), new Main.Stop("B", 0), new Main.Stop("C", 8)),
                Main.report("A", List.of(new Main.Leg("A", "B", 0), new Main.Leg("B", "C", 8))));
        for (var invalid : List.of(List.of(new Main.Leg("B", "A", 4)), List.of(new Main.Leg("A", "B", -1)),
                List.of(new Main.Leg("A", "B", Long.MAX_VALUE), new Main.Leg("B", "C", 1)))) {
            try {
                Main.report("A", invalid);
                throw new AssertionError("Expected an unusable leg");
            } catch (IllegalArgumentException expected) {
                same(true, expected.getMessage().startsWith("Unusable leg"));
            }
        }
        System.out.println("Practice checks passed");
    }
}
