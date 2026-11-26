public class ContractTests {
    static void expect(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected [" + expected + "], got [" + actual + "]");
        }
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        expect("0:2:4 3:3:4 4:4:4", Main.trace(sample, 28));
        expect("0:2:4 0:0:1", Main.trace(sample, 2));
        expect("0:0:1 1:1:1", Main.trace(new int[]{1, 2}, 2));
        expect("0:0:0", Main.trace(new int[]{3}, 4));
        expect("", Main.trace(new int[0], 0));
        System.out.println("Trace checks passed");
    }
}
