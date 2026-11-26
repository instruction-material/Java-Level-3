public class Main {
    static final class Node {
        final String data;
        Node next;

        Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    static boolean removeFirst(Node head, String target) {
        // TODO: Unlink only the earliest equal data node.
        throw new UnsupportedOperationException("Unlink only the earliest equal data node");
    }

    public static void main(String[] args) {
        Node head = new Node(null, new Node("a", new Node("b", new Node("a", null))));
        System.out.println(removeFirst(head, "a"));
        System.out.println(head.next.data);
        System.out.println(head.next.next.data);
    }
}
