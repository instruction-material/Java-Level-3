public class ContractTests {
    static void expect(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        if (args.length == 1 && args[0].equals("count")) {
            Main.SearchResult result = Main.measure(sample, 2);
            expect(0, result.index());
            expect(2, result.probes());
            expect(3, Main.measure(sample, 28).probes());
            expect(0, Main.measure(new int[0], 0).probes());
            System.out.println("Measurement checks passed");
            return;
        }
        expect(-1, Main.search(new int[0], 0));
        expect(0, Main.search(new int[]{7}, 7));
        expect(-1, Main.search(new int[]{7}, 6));
        expect(0, Main.search(sample, 2));
        expect(4, Main.search(sample, 38));
        expect(-1, Main.search(sample, 28));
        expect(3, Main.search(new int[]{1, 4, 8, 16}, 16));
        System.out.println("Iteration checks passed");
    }
}
