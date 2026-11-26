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
        var input = new java.util.ArrayList<>(List.of(8, 6, -2, 8));
        var expected = new java.util.ArrayList<>(List.of(-2, 6, 8, 8));
        Main.Algorithm sorting = new Main.Algorithm("example", values -> {
            values.sort(Integer::compare);
            return values;
        });
        if (Main.timed(sorting, input, expected) < 0) throw new AssertionError("Negative elapsed time");
        expect(List.of(8, 6, -2, 8), input);
        try {
            Main.timed(new Main.Algorithm("loses duplicates", values -> new java.util.ArrayList<>()),
                    input, expected);
            throw new AssertionError("Incorrect output must be rejected");
        } catch (IllegalStateException rejected) {
            if (!rejected.getMessage().startsWith("loses duplicates")) throw rejected;
        }
        System.out.println("Sorting checks passed");
    }
}
