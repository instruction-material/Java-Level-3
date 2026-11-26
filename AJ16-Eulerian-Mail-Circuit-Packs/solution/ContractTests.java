import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        expect(Main.RouteKind.NEITHER, Main.classify(0, List.of()));
        expect(Main.RouteKind.CIRCUIT, Main.classify(2, List.of()));
        var e = new Main.Street(0, 1);
        expect(Main.RouteKind.CIRCUIT, Main.classify(3, List.of(e, e)));
        expect(Main.RouteKind.CIRCUIT, Main.classify(3, List.of(new Main.Street(1, 1))));
        expect(Main.RouteKind.TRAIL, Main.classify(4, List.of(e, new Main.Street(1, 2))));
        expect(Main.RouteKind.NEITHER, Main.classify(4, List.of(e, e, new Main.Street(2, 3), new Main.Street(2, 3))));
        expect(Main.RouteKind.NEITHER, Main.classify(4, List.of(e, new Main.Street(0, 2), new Main.Street(0, 3))));

        System.out.println("Graph checks passed");
    }
}
