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

    enum Order { PREORDER, INORDER, POSTORDER }

    static List<Integer> preorder(Node root) {
        return traverse(root, Order.PREORDER);
    }

    static List<Integer> inorder(Node root) {
        return traverse(root, Order.INORDER);
    }

    static List<Integer> postorder(Node root) {
        return traverse(root, Order.POSTORDER);
    }

    private static List<Integer> traverse(Node root, Order order) {
        var result = new ArrayList<Integer>();
        collect(root, order, result);
        return result;
    }

    private static void collect(Node root, Order order, List<Integer> result) {
        if (root == null) return;
        if (order == Order.PREORDER) result.add(root.key);
        collect(root.left, order, result);
        if (order == Order.INORDER) result.add(root.key);
        collect(root.right, order, result);
        if (order == Order.POSTORDER) result.add(root.key);
    }

    public static void main(String[] args) {
        Node root = null;
        for (int key : new int[]{12, 3, 20, -4, 7, 16, 23}) root = add(root, key);
        System.out.println(preorder(root));
        System.out.println(inorder(root));
        System.out.println(postorder(root));
    }
}
