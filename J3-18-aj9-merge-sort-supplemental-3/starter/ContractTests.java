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
        int[] left = {1, 1, 4, 8};
        int[] right = {1, 2, 8, 9};
        expectArray(new int[]{1, 2, 4, 8, 9}, Main.union(left, right));
        expectArray(new int[]{1, 1, 4, 8}, left);
        expectArray(new int[]{1, 2, 8, 9}, right);
        expectArray(new int[0], Main.union(new int[0], new int[0]));
        expectArray(new int[]{Integer.MIN_VALUE}, Main.union(
                new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}, new int[0]));
        expectArray(new int[]{1, 3}, Main.union(new int[0], new int[]{1, 3, 3}));
        System.out.println("Sorting checks passed");
    }
}
