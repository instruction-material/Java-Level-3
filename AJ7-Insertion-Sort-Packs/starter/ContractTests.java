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
        expect(4L, Main.sort(new int[]{4, 1, 3, 1}));
            expect(0L, Main.sort(new int[]{1, 2, 3, 4}));
            expect(6L, Main.sort(new int[]{4, 3, 2, 1}));

        SplittableRandom random = new SplittableRandom(20261126L);
        for (int n = 0; n <= 48; n++) {
            for (int sample = 0; sample < 4; sample++) {
                int[] values = random.ints(n, -8, 9).toArray();
                if (sample == 1) Arrays.sort(values);
                if (sample == 2) for (int i = 0; i < n; i++) values[i] = n - i;
                if (sample == 3) Arrays.fill(values, -1);

                int[] expected = values.clone();
                Arrays.sort(expected);
                Main.sort(values);
                int[] actual = values;
                expectArray(expected, actual);

            }
        }
        System.out.println("Sorting checks passed");
    }
}
