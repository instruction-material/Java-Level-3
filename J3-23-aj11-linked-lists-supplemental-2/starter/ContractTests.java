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
        var last = new Main.Node("a", null);
        var middle = new Main.Node(null, last);
        var first = new Main.Node("a", middle);
        var head = new Main.Node(null, first);
        expect(true, Main.removeFirst(head, "a"));
        if (head.next != middle || middle.next != last) throw new AssertionError("Unlinked too much");
        expect(false, Main.removeFirst(head, "missing"));
        expect(true, Main.removeFirst(head, null));
        if (head.next != last) throw new AssertionError("Wrong link");
        expect(true, Main.removeFirst(head, "a"));
        expect(false, Main.removeFirst(head, null));
        System.out.println("List checks passed");
    }
}
