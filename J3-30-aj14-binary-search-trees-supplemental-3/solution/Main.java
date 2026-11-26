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

    static Node trim(Node root, int low, int high) {
        if (root == null || low > high) return null;
        if (root.key < low) return trim(root.right, low, high);
        if (root.key > high) return trim(root.left, low, high);
        root.left = trim(root.left, low, high);
        root.right = trim(root.right, low, high);
        return root;
    }

    public static void main(String[] args) {
        var root = add(null, 12);
        for (int key : new int[]{3, 20, 7, -4, 18, 23}) add(root, key);
        System.out.println(inorder(trim(root, 14, 23)));
    }
}
