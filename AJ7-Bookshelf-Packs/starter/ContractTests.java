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
        var a = new Main.Book("a", 180);
        var b = new Main.Book("b", 180);
        var c = new Main.Book("c", 250);
        var d = new Main.Book("d", 90);
        var input = new java.util.ArrayList<>(List.of(a, b, d, c));
        expect(List.of(c, a, b, d), Main.arrange(input));
        expect(List.of(a, b, d, c), input);
        expect(List.of(), Main.arrange(List.of()));
        expect(List.of(a), Main.arrange(List.of(a)));
        System.out.println("Sorting checks passed");
    }
}
