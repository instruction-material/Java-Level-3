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
        if (root == null) return new Node(key);
        Node current = root;
        while (key != current.key) {
            boolean left = key < current.key;
            Node child = left ? current.left : current.right;
            if (child == null) {
                if (left) current.left = new Node(key);
                else current.right = new Node(key);
                break;
            }
            current = child;
        }
        return root;
    }

    static boolean contains(Node root, int key) {
        Node current = root;
        while (current != null) {
            if (key == current.key) return true;
            current = key < current.key ? current.left : current.right;
        }
        return false;
    }

    static java.util.OptionalInt ceiling(Node root, int query) {
        var best = java.util.OptionalInt.empty();
        Node current = root;
        while (current != null) {
            if (query == current.key) return java.util.OptionalInt.of(current.key);
            if (query < current.key) {
                best = java.util.OptionalInt.of(current.key);
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return best;
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
