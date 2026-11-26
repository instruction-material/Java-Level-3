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
        var input = new java.util.ArrayList<>(List.of("Bea", "Ada", "Cora", "Ada"));
        expect(List.of("Ada", "Cora", "Ada", "Bea"), Main.reverse(input));
        expect(List.of("Ada", "Ada", "Bea", "Cora"), Main.alphabetize(input));
        expect(List.of("Bea", "Ada", "Cora", "Ada"), input);
        expect(List.of("Ada"), Main.reverse(List.of("Ada")));
        expect(List.of(), Main.reverse(List.of()));
        expect(List.of(), Main.alphabetize(List.of()));

        System.out.println("Collections checkpoint checks passed");
    }
}
