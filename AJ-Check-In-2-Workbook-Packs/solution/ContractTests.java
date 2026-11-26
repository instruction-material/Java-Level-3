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
        if (args.length == 1 && args[0].equals("range")) {
            expectArray(new int[]{0, 3}, Main.rangeAfterSorting(new int[]{8, 2, 5, 2, 2}, 2));
            expectArray(new int[]{-1, -1}, Main.rangeAfterSorting(new int[]{4, 2}, 3));
            expectArray(new int[]{1, 3}, Main.rangeAfterSorting(
                    new int[]{Integer.MAX_VALUE, 0, Integer.MAX_VALUE}, Integer.MAX_VALUE));
            System.out.println("Equal-range checks passed");
            return;
        }
        if (args.length == 1 && args[0].equals("bubble")) {
            expectArray(new int[]{12, 23, 12, 20, 43, 50}, Main.bubble(
                    new int[]{50, 23, 12, 43, 12, 20}, 2));
            int[] input = {3, 1, 3, -4};
            expectArray(new int[]{-4, 1, 3, 3}, Main.bubble(input, input.length));
            expectArray(new int[]{3, 1, 3, -4}, input);
            expectArray(new int[0], Main.bubble(new int[0], 5));
            System.out.println("Optional bubble checks passed");
            return;
        }
        expectArray(new int[]{14, 23, 76, 75, 34, 25}, Main.selection(
                new int[]{34, 23, 76, 75, 14, 25}, 2));
        expectArray(new int[]{13, 15, 27, 99, 8, 1}, Main.insertion(
                new int[]{99, 15, 27, 13, 8, 1}, 3));
        expectArray(new int[]{1, 2, 3, 4, 5, 7, 10, 11}, Main.merge(
                new int[]{1, 4, 7, 10}, new int[]{2, 3, 5, 11}));
        SplittableRandom random = new SplittableRandom(2026L);
        for (int size = 0; size < 50; size++) {
            int[] data = random.ints(size, -5, 6).toArray();
            int[] original = data.clone();
            int[] expected = data.clone();
            Arrays.sort(expected);
            expectArray(expected, Main.selection(data, size));
            expectArray(expected, Main.insertion(data, size));
            expectArray(expected, Main.sortMerge(data));
            expectArray(original, data);
        }
        System.out.println("Sorting checks passed");
    }
}
