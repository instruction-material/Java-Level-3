import java.util.Arrays;

public final class ContractTests {
    private ContractTests() {}

    public static void main(String[] args) {
        int[] sample = {7, 2, 7, 4};
        check(Main.firstIndex(sample, 7) == 0, "first of two equal values is index zero");
        check(Main.firstIndex(sample, 4) == 3, "last index");
        check(Main.firstIndex(sample, 9) == -1, "absent");
        check(Main.firstIndex(new int[0], 7) == -1, "empty");
        check(Main.firstIndex(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, Integer.MAX_VALUE) == 1, "integer extremes");
        try {
            Main.firstIndex(null, 0);
            throw new AssertionError("null array was accepted");
        } catch (IllegalArgumentException expected) {
            check(expected.getMessage() != null, "documented rejection");
        }
        if (args.length == 1 && args[0].equals("all")) {
            check(Arrays.equals(Main.allIndices(sample, 7), new int[]{0, 2}), "all matching indices");
            check(Main.allIndices(sample, 9).length == 0, "no matches");
            check(Main.allIndices(new int[0], 7).length == 0, "empty extension");
        }
        check(Arrays.equals(sample, new int[]{7, 2, 7, 4}), "array remains unchanged");
        System.out.println("Search contracts passed.");
    }

    private static void check(boolean ok, String description) {
        if (!ok) throw new AssertionError(description);
    }
}
