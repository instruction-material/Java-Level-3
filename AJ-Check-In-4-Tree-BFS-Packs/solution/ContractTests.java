import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException correct) {
            return;
        }
        throw new AssertionError("Expected an invalid input to be rejected");
    }

    public static void main(String[] args) {
        var tree = new Main.Node(12, new Main.Node(3, new Main.Node(-4, null, null), new Main.Node(7, null, null)),
                new Main.Node(20, new Main.Node(16, null, null), new Main.Node(23, null, null)));
        expect(List.of(), Main.breadthFirst(null));
        expect(List.of(12, 3, 20, -4, 7, 16, 23), Main.breadthFirst(tree));
        expect(List.of(12, 3, -4, 7, 20, 16, 23), Main.preorder(tree));
        var only = new Main.Node(99, null, null);
        expect(List.of(99), Main.breadthFirst(only));
        var chain = new Main.Node(1, null, new Main.Node(2, null, new Main.Node(3, null, null)));
        expect(List.of(1, 2, 3), Main.breadthFirst(chain));
        expect(3, tree.left().value());

        System.out.println("Advanced structures checks passed");
    }
}
