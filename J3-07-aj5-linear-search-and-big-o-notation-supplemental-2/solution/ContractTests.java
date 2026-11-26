import java.util.Arrays;

public final class ContractTests {
    private ContractTests() {}

    public static void main(String[] args) {
        String[] labels = {"map-north", "pen", "map-south"};
        check(Main.firstPrefix(labels, "map") == 0, "first prefix is at index zero");
        check(Main.firstPrefix(labels, "pen") == 1, "complete label is also a prefix");
        check(Main.firstPrefix(labels, "map-s") == 2, "last position");
        check(Main.firstPrefix(labels, "MAP") == -1, "case-sensitive");
        check(Main.firstPrefix(new String[0], "map") == -1, "empty list");
        for (String prefix : new String[]{null, ""}) {
            try {
                Main.firstPrefix(labels, prefix);
                throw new AssertionError("invalid prefix accepted");
            } catch (IllegalArgumentException expected) {
                check(expected.getMessage() != null, "documented rejection");
            }
        }
        check(Arrays.equals(labels, new String[]{"map-north", "pen", "map-south"}), "unchanged source");
        System.out.println("Prefix contracts passed.");
    }

    private static void check(boolean ok, String description) {
        if (!ok) throw new AssertionError(description);
    }
}
