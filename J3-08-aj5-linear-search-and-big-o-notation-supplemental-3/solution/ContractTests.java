import java.util.Arrays;

public final class ContractTests {
    private ContractTests() {}

    public static void main(String[] args) {
        int[] values = {7, 2, 7, 4};
        check(Main.search(values, 7).equals(new Main.Result(0, 1)), "first hit");
        check(Main.search(values, 4).equals(new Main.Result(3, 4)), "last hit");
        check(Main.search(values, -1).equals(new Main.Result(-1, 4)), "miss");
        check(Main.search(new int[0], 0).equals(new Main.Result(-1, 0)), "empty");
        check(Arrays.equals(values, new int[]{7, 2, 7, 4}), "unchanged input");
        for (int n : new int[]{8, 16, 32, 64}) {
            int[] allZero = new int[n];
            check(Main.search(allZero, 0).comparisons() == 1, "first value fixed");
            check(Main.search(allZero, 1).comparisons() == n, "miss scales with n");
        }
        System.out.println("Comparison-count contracts passed.");
    }

    private static void check(boolean ok, String description) {
        if (!ok) throw new AssertionError(description);
    }
}
