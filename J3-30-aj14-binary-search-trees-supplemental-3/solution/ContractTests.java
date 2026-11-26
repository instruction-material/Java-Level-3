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
        for (int key : new int[]{3, 20, 7, -4, 18, 23}) Main.add(root, key);
        var survivor = root.right;
        var left = survivor.left;
        var right = survivor.right;
        var result = Main.trim(root, 14, 23);
        expect(survivor, result);
        expect(left, result.left);
        expect(right, result.right);
        expect(List.of(18, 20, 23), Main.inorder(result));
        expect(left, Main.trim(result, 18, 18));
        expect(null, Main.trim(result, 21, 20));
        expect(null, Main.trim(null, 14, 23));

        System.out.println("Binary search tree checks passed");
    }
}
