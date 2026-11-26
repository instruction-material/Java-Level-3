public class ContractTests {
    static void expect(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 4, 4, 9};
        expect(0, Main.insertionIndex(new int[0], 4));
        expect(0, Main.insertionIndex(sample, 1));
        expect(1, Main.insertionIndex(sample, 4));
        expect(4, Main.insertionIndex(sample, 5));
        expect(4, Main.insertionIndex(sample, 9));
        expect(5, Main.insertionIndex(sample, 10));
        expect(0, Main.insertionIndex(new int[]{7, 7, 7}, 7));
        System.out.println("Insertion checks passed");
    }
}
