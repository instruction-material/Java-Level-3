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
        var input = new java.util.ArrayList<>(List.of("z", "a", "z", "z", "A"));
        expect(List.of(new Main.Dispatch("z", 3), new Main.Dispatch("a", 1), new Main.Dispatch("A", 1)),
                Main.batch(input));
        expect(List.of("z", "a", "z", "z", "A"), input);
        expect(List.of(), Main.batch(List.of()));

        System.out.println("Collections checkpoint checks passed");
    }
}
