public class ContractTests {
    static void expect(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        expect(-1, Main.search(new int[0], 0));
        expect(0, Main.search(new int[]{2}, 2));
        expect(-1, Main.search(new int[]{2}, 3));
        expect(0, Main.search(sample, 2));
        expect(4, Main.search(sample, 38));
        expect(-1, Main.search(sample, 39));
        expect(3, Main.search(new int[]{1, 4, 8, 16}, 16));
        expect(-1, Main.searchRange(sample, 2, 1, 4));
        System.out.println("Recursion checks passed");
    }
}
