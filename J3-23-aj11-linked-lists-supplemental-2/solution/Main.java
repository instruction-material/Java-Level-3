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
        Node previous = head;
        while (previous.next != null) {
            if (java.util.Objects.equals(previous.next.data, target)) {
                previous.next = previous.next.next;
                return true;
            }
            previous = previous.next;
        }
        return false;
    }

    public static void main(String[] args) {
        Node head = new Node(null, new Node("a", new Node("b", new Node("a", null))));
        System.out.println(removeFirst(head, "a"));
        System.out.println(head.next.data);
        System.out.println(head.next.next.data);
    }
}
