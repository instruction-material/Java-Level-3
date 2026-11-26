import java.util.NoSuchElementException;

public class Main {
    static final class IntQueue {
        private final int[] data;
        private int front;
        private int size;

        IntQueue(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
            data = new int[capacity];
        }

        int size() {
            return size;
        }

        void enqueue(int value) {
            // TODO: Locate the free slot and append.
            throw new UnsupportedOperationException("Locate the free slot and append");
        }

        int dequeue() {
            // TODO: Remove the oldest value and advance the front.
            throw new UnsupportedOperationException("Remove the oldest value and advance the front");
        }

        int peek() {
            // TODO: Read the oldest value without removing it.
            throw new UnsupportedOperationException("Read the oldest value without removing it");
        }
    }

    public static void main(String[] args) {
        var queue = new IntQueue(3);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println(queue.dequeue());
        queue.enqueue(40);
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
    }
}
