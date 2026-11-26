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
        var integer = new Main.Node<>(5);
        expect(5, integer.get());
        integer.set(6);
        expect(6, integer.get());
        var nullable = new Main.Node<String>("begin");
        nullable.set(null);
        expect("null", nullable.toString());
        var nodes = Main.mixed();
        expect(5, nodes.length);
        expect('C', nodes[0].get());
        expect("hello world", nodes[1].get());
        expect(true, nodes[2].get());
        expect(100, nodes[3].get());
        expect(3.1415, nodes[4].get());
        System.out.println("Sustainable-programming checks passed");
    }
}
