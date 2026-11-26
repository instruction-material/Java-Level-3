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
        int[] input = {7, -3, 7, 1};
        expect(true, Main.valid(input, new int[]{-3, 1, 7, 7}));
        expect(false, Main.valid(input, new int[]{-3, 1, 1, 7}));
        expect(false, Main.valid(input, new int[]{-3, 7, 1, 7}));
        expect(false, Main.valid(input, new int[]{-3, 1, 7}));
        expect(true, Main.valid(new int[0], new int[0]));
        expectArray(new int[]{7, -3, 7, 1}, input);
        System.out.println("Sorting checks passed");
    }
}
