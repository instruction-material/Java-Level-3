import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Node(int value, Node left, Node right) {}

    static List<Integer> breadthFirst(Node root) {
        // TODO: Visit a tree level by level with a queue.
        throw new UnsupportedOperationException("Visit a tree level by level with a queue");
    }

    static List<Integer> preorder(Node root) {
        var items = new ArrayList<Integer>();
        appendPreorder(root, items);
        return items;
    }

    private static void appendPreorder(Node root, List<Integer> items) {
        if (root == null) return;
        items.add(root.value());
        appendPreorder(root.left(), items);
        appendPreorder(root.right(), items);
    }

    public static void main(String[] args) {
        var root = new Node(12, new Node(3, null, new Node(7, null, null)), new Node(20, null, null));
        System.out.println(breadthFirst(root));
        System.out.println(preorder(root));
    }
}
