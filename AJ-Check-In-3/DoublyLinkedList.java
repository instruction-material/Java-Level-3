import java.util.Objects;

public class DoublyLinkedList<T> {

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

    ////////////////////////////////
    // NEW METHOD:
    ///////////////////////////////
    public boolean remove(T item) {
        for (Node<T> current = head.next; current != tail; current = current.next) {
            if (Objects.equals(item, current.data)) {
                doublyLink(current.previous, current.next);
                this.length--;
                return true;
            }
        }
        return false;
    }
    ////////////////////////////////////

    public int getLength() {
        return this.length;
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    private void doublyLink(Node<T> first, Node<T> second) {
        first.next = second;
        second.previous = first;
    }

    public void insertFront(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldFront = this.head.next;
        doublyLink(head, newNode);
        doublyLink(newNode, oldFront);

        this.length++;
    }

    public void insertBack(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldBack = tail.previous;
        doublyLink(oldBack, newNode);
        doublyLink(newNode, tail);

        this.length++;
    }

    public void removeFront() {
        Node<T> oldFront = this.head.next;
        if (oldFront != tail) {

            Node<T> newFront = oldFront.next;
            doublyLink(head, newFront);

            this.length--;
        }
    }

    public void removeBack() {

        Node<T> oldBack = this.tail.previous;
        if (oldBack != head) {

            Node<T> newBack = oldBack.previous;
            doublyLink(newBack, tail);

            this.length--;
        }
    }

    public T get(int index) {
        Objects.checkIndex(index, length);
        Node<T> current = head.next;
        for (int position = 0; position < index; position++) {
            current = current.next;
        }
        return current.data;
    }

    public boolean clearAll() {
        doublyLink(head, tail);
        length = 0;
        return true;
    }

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
