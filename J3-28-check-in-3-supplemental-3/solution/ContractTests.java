import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        expect(Map.of(), Main.replay(List.of()));
        expect(Map.of(), Main.replay(List.of("UNDO", "lamp", "UNDO", "UNDO")));
        expect(Map.of("lamp", 1), Main.replay(List.of("lamp", "lamp", "bulb", "UNDO", "UNDO")));
        expect(Map.of("bulb", 1), Main.replay(List.of("lamp", "bulb", "UNDO", "UNDO", "bulb")));
        expect(List.of("a", "b"), List.copyOf(Main.replay(List.of("b", "a")).keySet()));

        System.out.println("Collections checkpoint checks passed");
    }
}
