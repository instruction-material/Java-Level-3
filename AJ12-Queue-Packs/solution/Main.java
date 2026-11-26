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
            if (size == data.length) throw new IllegalStateException("Queue is full");
            int rear = (int) (((long) front + size) % data.length);
            data[rear] = value;
            size++;
        }

        int dequeue() {
            int oldest = peek();
            front = (front + 1) % data.length;
            size--;
            return oldest;
        }

        int peek() {
            if (size == 0) throw new NoSuchElementException("Queue is empty");
            return data[front];
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
