import java.util.ArrayList;
import java.util.List;

public class Main {
    static final class Node {
        final int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }

    static List<Integer> inorder(Node root) {
        var keys = new ArrayList<Integer>();
        collect(root, keys);
        return keys;
    }

    private static void collect(Node root, List<Integer> keys) {
        if (root == null) return;
        collect(root.left, keys);
        keys.add(root.key);
        collect(root.right, keys);
    }

    static Node insert(Node root, int key) {
        // TODO: Insert a distinct key into the ordered tree.
        throw new UnsupportedOperationException("Insert a distinct key into the ordered tree");
    }

    static boolean contains(Node root, int key) {
        // TODO: Follow one branch at each comparison.
        throw new UnsupportedOperationException("Follow one branch at each comparison");
    }

    static java.util.OptionalInt ceiling(Node root, int query) {
        // TODO: Track the smallest candidate at least as large as the query.
        throw new UnsupportedOperationException("Track the smallest candidate at least as large as the query");
    }

    public static void main(String[] args) {
        Node root = null;
        for (int key : new int[]{12, 3, 20, 7, 3}) root = insert(root, key);
        if (args.length == 1 && args[0].equals("ceiling")) {
            System.out.println(ceiling(root, 8).orElseThrow());
            return;
        }
        System.out.println(inorder(root));
        System.out.println(contains(root, 7));
        System.out.println(contains(root, 9));
    }
}
