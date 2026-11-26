import java.util.Objects;

public class LinkedList<T> implements ListContract<T> {

    private Node<T> head;
    private int length;

    private class Node<T> {
        private T data;
        private Node<T> next;

        //default constructor
        public Node() {
        }
        //constructor with data
        public Node(T data) {
            this.data = data;
        }
    }

    public LinkedList() {
        this.head = new Node<>();
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

    @Override
    public void insertFront(T data) {
        // TODO: Insert one node after the sentinel.
        throw new UnsupportedOperationException("Insert one node after the sentinel");
    }

    @Override
    public void insertBack(T data) {
        // TODO: Walk to the end and append a node.
        throw new UnsupportedOperationException("Walk to the end and append a node");
    }

    @Override
    public void removeFront() {
        // TODO: Skip the first node when one exists.
        throw new UnsupportedOperationException("Skip the first node when one exists");
    }

    @Override
    public void removeBack() {
        // TODO: Unlink the final node when one exists.
        throw new UnsupportedOperationException("Unlink the final node when one exists");
    }

    @Override
    public T get(int index) {
        // TODO: Check and traverse to the requested position.
        throw new UnsupportedOperationException("Check and traverse to the requested position");
    }

    @Override
    public boolean clearAll() {
        // TODO: Reset the sentinel and count.
        throw new UnsupportedOperationException("Reset the sentinel and count");
    }

    public int firstIndex(T value) {
        // TODO: Find the first equal value.
        throw new UnsupportedOperationException("Find the first equal value");
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (Node<T> n = this.head.next; n != null; n = n.next) {
            s.append(n.data);
            if (n.next != null) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }
}
