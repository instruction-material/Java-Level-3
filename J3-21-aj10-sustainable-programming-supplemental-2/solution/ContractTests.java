import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;

public class ContractTests {

    static void expect(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void expectArray(int[] expected, int[] actual) {
        expect(Arrays.toString(expected), Arrays.toString(actual));
    }

    public static void main(String[] args) {
        expect(new Main.Stock("flour", 12), Main.parse(" flour | 12 "));
        expect(new Main.Stock("lemon", 0), Main.parse("lemon|0"));
        for (String invalid : new String[]{null, "x", "|2", "x|", "x|-1", "x|3|4", "x|3.5"}) {
            try {
                Main.parse(invalid);
                throw new AssertionError("Expected rejection: " + invalid);
            } catch (IllegalArgumentException expected) {
                if (expected.getMessage().isBlank()) throw new AssertionError("Missing error message");
            }
        }
        System.out.println("Sustainable-programming checks passed");
    }
}
