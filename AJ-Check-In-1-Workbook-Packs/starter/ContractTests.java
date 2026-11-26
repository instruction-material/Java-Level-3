import java.util.List;

public class ContractTests {
    private static void expect(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("memo")) {
            int[][] cache = new int[11][11];
            expect(252, Main.pascalMemo(10, 5, cache));
            expect(252, cache[10][5]);
            expect(252, Main.pascalMemo(10, 5, cache));
            expect(1, Main.pascalMemo(10, 0, cache));
            expect(0, Main.pascalMemo(10, 11, cache));
            System.out.println("Memo checks passed");
            return;
        }
        expect(0, Main.stringLength(""));
        expect(12, Main.stringLength("hello world!"));
        expect(6, Main.pascal(4, 2));
        expect(1, Main.pascal(0, 0));
        expect(0, Main.pascal(4, 5));
        expect(List.of(5, 4, 3, 2, 1, 2, 3, 2, 1), Main.trace(5));
        expect(true, Main.linearContains(new int[]{9, 2, 8}, 2));
        expect(false, Main.linearContains(new int[]{9, 2, 8}, 3));
        expect(false, Main.linearContains(new int[0], 2));
        int[] values = {2, 4, 8, 16};
        expect(0, Main.binaryIndex(values, 2));
        expect(3, Main.binaryIndex(values, 16));
        expect(-1, Main.binaryIndex(values, 7));
        expect(-1, Main.binaryIndex(new int[0], 7));
        expect(0, Main.binaryRange(values, 2, 0, 3));
        expect(3, Main.binaryRange(values, 16, 0, 3));
        expect(-1, Main.binaryRange(values, 7, 0, 3));
        expect(-1, Main.binaryRange(new int[0], 7, 0, -1));
        System.out.println("Workbook checks passed");
    }
}
