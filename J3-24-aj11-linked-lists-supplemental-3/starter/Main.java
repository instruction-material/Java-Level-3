public class Main {
    static final class Node {
        final int data;
        Node next;

        Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    static Node reverse(Node head) {
        // TODO: Redirect the links one at a time.
        throw new UnsupportedOperationException("Redirect the links one at a time");
    }

    public static void main(String[] args) {
        Node head = new Node(1, new Node(2, new Node(3, null)));
        for (Node node = reverse(head); node != null; node = node.next) {
            System.out.println(node.data);
        }
    }
}
