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
