public class LinkedList<T> {

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

    ////////////////////////////////
    // NEW METHOD:
    ///////////////////////////////
    public int indexOf(T item) {
        int index = 0;
        Node<T> current = this.head;
        while (current.next != null) {
            current = current.next;

            // check for a match
            if (item.equals(current.data)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    ////////////////////////////////////

    public int getLength() {
        return this.length;
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    public void insertFront(T data) {

        //create a new node
        Node<T> newNode = new Node<T>(data);

        //insert the new node
        Node<T> oldFront = this.head.next;
        this.head.next = newNode;
        newNode.next = oldFront;

        this.length++;
    }

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

    public void removeFront() {
        Node<T> oldFront = this.head.next;
        if (oldFront != null) {
            Node<T> newFront = oldFront.next;
            this.head.next = newFront;
            this.length--;
        }
    }

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

    public T get(int index) {
        Node<T> current = this.head.next;

        while (index > 0) {
            //throw an exception if you've reached the end of the list
            if (current == null) {
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

    public boolean clearAll() {
        //base case
        if (this.head.next == null) {
            return true;
        }
        //recursive case
        this.removeBack();
        return this.clearAll();
    }

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
