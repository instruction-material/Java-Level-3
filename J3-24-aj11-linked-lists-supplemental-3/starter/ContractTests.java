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
        var third = new Main.Node(7, null);
        var second = new Main.Node(7, third);
        var first = new Main.Node(1, second);
        var reversed = Main.reverse(first);
        if (reversed != third || third.next != second || second.next != first || first.next != null) {
            throw new AssertionError("Wrong link or replacement nodes");
        }
        if (Main.reverse(reversed) != first || first.next != second || third.next != null) {
            throw new AssertionError("Second reverse should restore the source");
        }
        if (Main.reverse(null) != null || Main.reverse(third) != third) {
            throw new AssertionError("Empty or singleton");
        }
        System.out.println("List checks passed");
    }
}
