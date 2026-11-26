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
        expect(4L, Main.inversions(new int[]{4, 1, 3, 1}));
        expect(0L, Main.inversions(new int[]{1, 1, 3, 4}));
        expect(0L, Main.inversions(new int[0]));
        expect(6L, Main.inversions(new int[]{4, 3, 2, 1}));
        int[] input = {5, -1, 0};
        expect(2L, Main.inversions(input));
        expectArray(new int[]{5, -1, 0}, input);
        System.out.println("Sorting checks passed");
    }
}
