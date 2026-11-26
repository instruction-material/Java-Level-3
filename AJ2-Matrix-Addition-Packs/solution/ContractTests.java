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
        int[][] left = {{1, 2}, {3, 4}};
        same("[[5, 5], [5, 5]]", Arrays.deepToString(Main.add(left, new int[][]{{4, 3}, {2, 1}})));
        same("[[1, 2], [3, 4]]", Arrays.deepToString(left));
        same("[]", Arrays.deepToString(Main.add(new int[0][], new int[0][])));
        invalid(() -> Main.add(new int[][]{{1}, {2, 3}}, new int[][]{{0}, {4, 5}}));
        invalid(() -> Main.add(new int[][]{{Integer.MAX_VALUE}}, new int[][]{{1}}));
        System.out.println("Review checks passed");
    }
}
