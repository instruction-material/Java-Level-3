import java.util.List;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }
    public static void main(String[] args) {
        var ab = new Main.Road("A", "B");
        var ba = new Main.Road("B", "A");
        var cd = new Main.Road("C", "D");
        var dc = new Main.Road("D", "C");
        var self = new Main.Road("A", "A");
        same(List.of(), Main.audit(java.util.Map.of()));
        same(List.of(), Main.audit(java.util.Map.of(ab, 1200, ba, 1200, self, 0)));
        same(List.of(new Main.Finding(ab, "different reverse cost"), new Main.Finding(ba, "different reverse cost"),
                        new Main.Finding(cd, "one-way")),
                Main.audit(java.util.Map.of(cd, 6, ab, 3, ba, 4, self, 0)));
        same(List.of(new Main.Finding(cd, "one-way")), Main.audit(java.util.Map.of(cd, 0)));
        same(List.of(), Main.audit(java.util.Map.of(cd, 0, dc, 0)));
        System.out.println("Practice checks passed");
    }
}
