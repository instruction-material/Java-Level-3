import java.lang.IndexOutOfBoundsException;

public class DoublyLinkedList<T> implements List<T> {

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
        first.next = second;
        second.previous = first;
    }

    @Override
    public void insertFront(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldFront = this.head.next;
        doublyLink(head, newNode);
        doublyLink(newNode, oldFront);

        this.length++;
    }

    @Override
    public void insertBack(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldBack = tail.previous;
        doublyLink(oldBack, newNode);
        doublyLink(newNode, tail);

        this.length++;
    }

    @Override
    public void removeFront() {
        Node<T> oldFront = this.head.next;
        if (oldFront != tail) {

            Node<T> newFront = oldFront.next;
            doublyLink(head, newFront);

            this.length--;
        }
    }

    @Override
    public void removeBack() {

        Node<T> oldBack = this.tail.previous;
        if (oldBack != head) {

            Node<T> newBack = oldBack.previous;
            doublyLink(newBack, tail);

            this.length--;
        }
    }
    @Override
    public T get(int index) {
        Node<T> current = this.head.next;

        while (index > 0) {
            //throw an exception if you've reached the end of the list
            if (current == tail) {
                throw new IndexOutOfBoundsException();
            }
            //decrement the index as you traverse the list
            current = current.next;
            index--;
        }
        //once you've traversed "index" number of Nodes
        //return the data in that Node
        return current.data;
    }

    @Override
    public boolean clearAll() {
        //base case
        if (this.head.next == tail) {
            return true;
        }
        //recursive case
        this.removeBack();
        return this.clearAll();
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
