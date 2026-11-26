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
        expect(List.of("[0,3)", "[0,1)", "[1,3)", "[1,2)", "[2,3)"), Main.trace(3));
        expect(List.of("[0,1)"), Main.trace(1));
        expect(List.of(), Main.trace(0));
        for (int size = 1; size <= 16; size++) expect(2 * size - 1, Main.trace(size).size());
        try {
            Main.trace(-1);
            throw new AssertionError("Negative size");
        } catch (IllegalArgumentException expected) {
            // A negative length is outside the contract.
        }
        System.out.println("Sorting checks passed");
    }
}
