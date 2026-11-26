import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Node(int value, Node left, Node right) {}

    static List<Integer> breadthFirst(Node root) {
        if (root == null) return List.of();
        var queue = new ArrayDeque<Node>();
        var result = new ArrayList<Integer>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            Node current = queue.removeFirst();
            result.add(current.value());
            if (current.left() != null) queue.addLast(current.left());
            if (current.right() != null) queue.addLast(current.right());
        }
        return result;
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
