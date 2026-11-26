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

    static Node add(Node root, int key) {
        if (root == null) return new Node(key);
        if (key < root.key) root.left = add(root.left, key);
        if (key > root.key) root.right = add(root.right, key);
        return root;
    }

    static List<Integer> within(Node root, int low, int high) {
        var keys = new ArrayList<Integer>();
        if (low <= high) collect(root, low, high, keys);
        return keys;
    }

    private static void collect(Node root, int low, int high, List<Integer> keys) {
        // TODO: Visit only branches that can contain an in-range value.
        throw new UnsupportedOperationException("Visit only branches that can contain an in-range value");
    }

    public static void main(String[] args) {
        var root = add(null, 12);
        for (int key : new int[]{3, 20, 7, -4, 18, 23}) add(root, key);
        System.out.println(within(root, 7, 20));
    }
}
