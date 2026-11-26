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
        int[] input = {4, 2, 3, 1};
        List<int[]> result = Main.snapshots(input);
        expect(3, result.size());
        expectArray(new int[]{2, 3, 1, 4}, result.get(0));
        expectArray(new int[]{2, 1, 3, 4}, result.get(1));
        expectArray(new int[]{1, 2, 3, 4}, result.get(2));
        expectArray(new int[]{4, 2, 3, 1}, input);
        expect(0, Main.snapshots(new int[0]).size());
        expect(0, Main.snapshots(new int[]{9}).size());
        System.out.println("Sorting checks passed");
    }
}
