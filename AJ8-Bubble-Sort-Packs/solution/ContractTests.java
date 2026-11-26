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
        if (args.length == 1 && args[0].equals("last")) {
            int[] input = {2, 1, 3, 4, 5};
            expect(4L, Main.lastExchange(input));
            expectArray(new int[]{1, 2, 3, 4, 5}, input);
            expect(0L, Main.lastExchange(new int[0]));
            SplittableRandom random = new SplittableRandom(2026L);
            for (int size = 0; size < 40; size++) {
                input = random.ints(size, -6, 7).toArray();
                int[] expected = input.clone();
                Arrays.sort(expected);
                Main.lastExchange(input);
                expectArray(expected, input);
            }
            System.out.println("Last-exchange checks passed");
            return;
        }
        expect(10L, Main.sort(new int[]{1, 2, 3, 4, 5}, false));
        expect(4L, Main.sort(new int[]{1, 2, 3, 4, 5}, true));
        expect(10L, Main.sort(new int[]{5, 4, 3, 2, 1}, true));
        expect(7L, Main.sort(new int[]{2, 1, 3, 4, 5}, true));
        expect(0L, Main.sort(new int[0], true));
        int[] sample = {-1, 3, -2, 3, -2};
        Main.sort(sample, false);
        expectArray(new int[]{-2, -2, -1, 3, 3}, sample);
        System.out.println("Sorting checks passed");
    }
}
