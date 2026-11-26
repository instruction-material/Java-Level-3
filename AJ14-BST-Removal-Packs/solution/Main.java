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

    static final class Tree {
        private Node root;
        private int size;

        int size() { return size; }
        List<Integer> keys() { return inorder(root); }

        void insert(int key) {
            if (has(root, key)) return;
            root = add(root, key);
            size++;
        }

        boolean remove(int key) {
            if (!has(root, key)) return false;
            root = replacement(root, key);
            size--;
            return true;
        }

        void clear() {
            clearTree(this);
        }
    }

    private static boolean has(Node root, int key) {
        for (Node node = root; node != null; node = key < node.key ? node.left : node.right) {
            if (node.key == key) return true;
        }
        return false;
    }

    static Node replacement(Node root, int key) {
        if (root == null) return null;
        if (key < root.key) root.left = replacement(root.left, key);
        else if (key > root.key) root.right = replacement(root.right, key);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            Node predecessor = root.left;
            while (predecessor.right != null) predecessor = predecessor.right;
            var next = new Node(predecessor.key);
            next.left = replacement(root.left, predecessor.key);
            next.right = root.right;
            return next;
        }
        return root;
    }

    static void clearTree(Tree tree) {
        tree.root = null;
        tree.size = 0;
    }

    public static void main(String[] args) {
        var tree = new Tree();
        for (int key : new int[]{12, 3, 20, 7, 16}) tree.insert(key);
        tree.remove(12);
        System.out.println(tree.keys());
        System.out.println(tree.remove(90));
        tree.clear();
        System.out.println(tree.size());
    }
}
