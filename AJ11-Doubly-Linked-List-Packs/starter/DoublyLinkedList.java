import java.util.Objects;

public class DoublyLinkedList<T> implements ListContract<T> {

    private Node<T> head;
    private Node<T> tail;
    private int length;

    private class Node<T> {
        private T data;
        private Node<T> previous;
        private Node<T> next;

        //default constructor
        public Node() {
        }
        //constructor with data
        public Node(T data) {
            this.data = data;
        }
    }

    public DoublyLinkedList() {
        this.head = new Node<>();
        this.tail = new Node<>();
        head.next = tail;
        tail.previous = head;
        this.length = 0;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    private void doublyLink(Node<T> first, Node<T> second) {
        // TODO: Connect both directions between two neighbors.
        throw new UnsupportedOperationException("Connect both directions between two neighbors");
    }

    @Override
    public void insertFront(T data) {
        // TODO: Insert between head and its successor.
        throw new UnsupportedOperationException("Insert between head and its successor");
    }

    @Override
    public void insertBack(T data) {
        // TODO: Insert between the last node and tail.
        throw new UnsupportedOperationException("Insert between the last node and tail");
    }

    @Override
    public void removeFront() {
        // TODO: Reconnect head to the second node or tail.
        throw new UnsupportedOperationException("Reconnect head to the second node or tail");
    }

    @Override
    public void removeBack() {
        // TODO: Reconnect tail to the penultimate node or head.
        throw new UnsupportedOperationException("Reconnect tail to the penultimate node or head");
    }
    @Override
    public T get(int index) {
        // TODO: Check and traverse to the requested position.
        throw new UnsupportedOperationException("Check and traverse to the requested position");
    }

    @Override
    public boolean clearAll() {
        // TODO: Reconnect both sentinels and reset length.
        throw new UnsupportedOperationException("Reconnect both sentinels and reset length");
    }

    public java.util.List<T> reverseValues() {
        // TODO: Walk backward through only the data nodes.
        throw new UnsupportedOperationException("Walk backward through only the data nodes");
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (Node<T> n = this.head.next; n != tail; n = n.next) {
            s.append(n.data);
            if (n.next != tail) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }
}
