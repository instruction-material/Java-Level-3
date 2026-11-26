import java.util.*;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void invalid(Runnable work) {
        try {
            work.run();
        } catch (IllegalArgumentException | ArithmeticException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input");
    }

    public static void main(String[] args) {
        int[] values = {3, -2, 5};
        same("[6, -4, 10]", Arrays.toString(Main.doubled(values)));
        same("[3, -2, 5]", Arrays.toString(values));
        same("[]", Arrays.toString(Main.doubled(new int[0])));
        same("[4294967294]", Arrays.toString(Main.doubled(new int[]{Integer.MAX_VALUE})));
        same(2L, Main.sumEvens(new int[]{3, -2, 4}));
        same(0L, Main.sumEvens(new int[]{1, 3}));
        same(6L, Main.totalLengths(new String[]{"oak", "", "elm"}));
        same(0L, Main.totalLengths(new String[]{}));
        double[] sample = Main.randomValues(4, new Random(2));
        same(4, sample.length);
        same(true, Arrays.stream(sample).allMatch(value -> value >= 15 && value < 100));
        same(Arrays.toString(sample), Arrays.toString(Main.randomValues(4, new Random(2))));
        same(0, Main.randomValues(0, new Random(2)).length);
        invalid(() -> Main.randomValues(-1, new Random(2)));
        System.out.println("Review checks passed");
    }
}
