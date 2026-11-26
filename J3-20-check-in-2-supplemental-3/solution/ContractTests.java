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
        int[] input = {8, -3, 5, -3, 2};
        expectArray(new int[]{-3, -3, 2}, Main.smallest(input, 3));
        expectArray(new int[]{-3, -3, 2, 5, 8}, Main.smallest(input, 5));
        expectArray(new int[0], Main.smallest(input, 0));
        expectArray(new int[]{8, -3, 5, -3, 2}, input);
        for (int invalid : new int[]{-1, 6}) {
            try {
                Main.smallest(input, invalid);
                throw new AssertionError("Out-of-range k accepted");
            } catch (IllegalArgumentException expected) {
                // Only selections within the input size are defined.
            }
        }
        System.out.println("Sorting checks passed");
    }
}
