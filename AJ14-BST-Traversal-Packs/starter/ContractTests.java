import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var root = Main.add(null, 12);
        for (int key : new int[]{3, 20, -4, 7, 16, 23}) Main.add(root, key);
        expect(List.of(12, 3, -4, 7, 20, 16, 23), Main.preorder(root));
        expect(List.of(-4, 3, 7, 12, 16, 20, 23), Main.inorder(root));
        expect(List.of(-4, 7, 3, 16, 23, 20, 12), Main.postorder(root));
        expect(12, root.key);
        expect(List.of(5), Main.postorder(new Main.Node(5)));
        expect(List.of(), Main.preorder(null));
        expect(List.of(), Main.inorder(null));
        expect(List.of(), Main.postorder(null));

        System.out.println("Binary search tree checks passed");
    }
}
