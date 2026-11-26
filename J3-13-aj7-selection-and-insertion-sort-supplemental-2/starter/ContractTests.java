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
        var a = new Main.Task("a", 5);
        var b = new Main.Task("b", -1);
        var c = new Main.Task("c", 5);
        var d = new Main.Task("d", -1);
        Main.Task[] input = {a, b, c, d};
        expect(List.of(b, d, a, c), Arrays.asList(Main.order(input)));
        expect(List.of(a, b, c, d), Arrays.asList(input));
        expect(0, Main.order(new Main.Task[0]).length);
        System.out.println("Sorting checks passed");
    }
}
