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

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldFront = this.head.next;
        this.head.next = newNode;
        newNode.next = oldFront;

        this.length++;
    }

    @Override
    public void insertBack(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //traverse the list to find the last node
        Node<T> current = this.head;
        while (current.next != null) {
            current = current.next;
        }

        //insert the new node
        current.next = newNode;
        this.length++;
    }

    @Override
    public void removeFront() {
        Node<T> oldFront = this.head.next;
        if (oldFront != null) {
            Node<T> newFront = oldFront.next;
            this.head.next = newFront;
            this.length--;
        }
    }

    @Override
    public void removeBack() {

        //find the last node...
        //and the node previous to the last node (second to last)
        Node<T> previous = null;
        Node<T> current = this.head;
        while (current.next != null) {
            previous = current;
            current = current.next;
        }

        //if the second to last node isn't null...
        //make the second to last node point to null...
        //to delete the last node
        if (previous != null) {
            previous.next = null;
            this.length--;
        }
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, length);
        Node<T> current = head.next;
        for (int position = 0; position < index; position++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public boolean clearAll() {
        head.next = null;
        length = 0;
        return true;
    }

    public int firstIndex(T value) {
        int position = 0;
        for (Node<T> node = head.next; node != null; node = node.next) {
            if (Objects.equals(node.data, value)) return position;
            position++;
        }
        return -1;
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
