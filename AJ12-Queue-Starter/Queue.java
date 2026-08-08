public class Queue {
    private final int MAX; // maximum capacity of the queue
    private int queue[];   // array to store queue elements
    private int front;     // the start of the queue
    private int nextBack;  // where to insert the next item in the back
    private int size;      // current size of the queue

    // Constructor to initialize queue
    public Queue() {
        this.MAX = 0;
        this.queue = new int[MAX];
        this.front = 0;
        this.nextBack = 0;
        this.size = 0;
    }

    // Returns whether the queue is empty or not
    public Boolean isEmpty() {
        return true;
    }

    // Returns whether the queue is full or not
    public Boolean isFull() {
        return true;
    }

    // Remove front element from the queue
    public void dequeue() {
    }

    // Add an item to the back of the queue
    public void enqueue(int item) {
    }

    // Returns the front element in the queue
    public int front() {
        return -1;
    }

    // Returns the size of the queue
    public int size() {
        return -1;
    }

    @Override
    public String toString() {
        return "";
    }
}
