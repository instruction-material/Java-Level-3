import java.util.*;

// Class for queue
public class Queue {
    private final int MAX; // maximum capacity of the queue
    private int[] queue;   // array to store queue elements
    private int front;     // the start of the queue
    private int nextBack;  // index for inserting the next back item
    private int size;      // current size of the queue

    // Constructor to initialize queue
    public Queue(int maxSize) {
        this.MAX = maxSize;
        this.queue = new int[MAX];
        this.front = 0;
        this.nextBack = 0;
        this.size = 0;
    }

    // return if the queue is empty or not
    public Boolean isEmpty() {
        return size == 0;
    }

    // return if the queue is full or not
    public Boolean isFull() {
        return size == MAX;
    }

    // Remove front element from the queue
    public void dequeue() {
        if (!isEmpty()) {
            front = (front + 1) % MAX;
            size--;
        } else {
            System.out.println("UnderFlow");
            throw new UnderflowException();
        }
    }

    // add an item to the back of the queue
    public void enqueue(int item) {
        if (!isFull()) {
            queue[nextBack] = item;
            nextBack = (nextBack + 1) % MAX;
            size++;
        } else {
            System.out.println("OverFlow");
            throw new OverflowException();
        }
    }

    //return front element in the queue
    public int front() {
        if (!isEmpty()) {
            int index = front % MAX;
            return queue[index];
        } else {
            System.out.println("UnderFlow");
            throw new UnderflowException();
        }
    }

    // returns the size of the queue
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < size; i++) {
            int index = (front + i) % MAX;
            result += queue[index] + " ";
        }
        return result;
    }
}
