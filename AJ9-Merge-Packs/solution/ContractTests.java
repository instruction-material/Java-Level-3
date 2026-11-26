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
        if (args.length == 1 && args[0].equals("stable")) {
            var a = new Main.Entry(1, "a");
            var b = new Main.Entry(4, "b");
            var c = new Main.Entry(4, "c");
            var d = new Main.Entry(6, "d");
            Main.Entry[] left = {a, b};
            Main.Entry[] right = {c, d};
            expect(List.of(a, b, c, d), Arrays.asList(Main.mergeEntries(left, right)));
            expect(List.of(a, b), Arrays.asList(left));
            expect(List.of(c, d), Arrays.asList(right));
            expect(List.of(c, d), Arrays.asList(Main.mergeEntries(new Main.Entry[0], right)));
            System.out.println("Stable merge checks passed");
            return;
        }
        int[] left = {-2, 1, 5};
        int[] right = {1, 2, 7, 9};
        expectArray(new int[]{-2, 1, 1, 2, 5, 7, 9}, Main.merge(left, right));
        expectArray(new int[]{-2, 1, 5}, left);
        expectArray(new int[]{1, 2, 7, 9}, right);
        expectArray(right, Main.merge(new int[0], right));
        expectArray(left, Main.merge(left, new int[0]));
        expectArray(new int[0], Main.merge(new int[0], new int[0]));
        System.out.println("Sorting checks passed");
    }
}
