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
        int[] input = {-1, 8, 3, 8, 0};
        expect(List.of(List.of(-1), List.of(8), List.of(3), List.of(8), List.of(0)), Main.leaves(input));
        expectArray(new int[]{-1, 8, 3, 8, 0}, input);
        expect(List.of(), Main.leaves(new int[0]));
        expect(List.of(List.of(4)), Main.leaves(new int[]{4}));
        expect(List.of(List.of(7), List.of(1)), Main.leaves(new int[]{7, 1}));
        System.out.println("Sorting checks passed");
    }
}
