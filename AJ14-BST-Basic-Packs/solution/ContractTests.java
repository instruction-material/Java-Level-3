import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var root = Main.insert(null, 12);
        for (int key : new int[]{3, 20, 7, -9, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            expect(root, Main.insert(root, key));
        }
        expect(root, Main.insert(root, 3));
        expect(List.of(Integer.MIN_VALUE, -9, 3, 7, 12, 20, Integer.MAX_VALUE), Main.inorder(root));
        expect(true, Main.contains(root, Integer.MAX_VALUE));
        expect(true, Main.contains(root, Integer.MIN_VALUE));
        expect(false, Main.contains(root, 13));
        expect(false, Main.contains(null, 0));
        if (args.length == 1 && args[0].equals("ceiling")) {
            expect(java.util.OptionalInt.of(12), Main.ceiling(root, 8));
            expect(java.util.OptionalInt.of(7), Main.ceiling(root, 7));
            expect(java.util.OptionalInt.of(Integer.MIN_VALUE), Main.ceiling(root, Integer.MIN_VALUE));
            expect(java.util.OptionalInt.empty(), Main.ceiling(root.left, 20));
            expect(java.util.OptionalInt.empty(), Main.ceiling(null, 0));
            System.out.println("Ceiling checks passed");
            return;
        }

        System.out.println("Binary search tree checks passed");
    }
}
