import java.util.NoSuchElementException;

public class Main {
    static final class IntDeque {
        private final int[] data;
        private int front;
        private int size;

        IntDeque(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
            data = new int[capacity];
        }

        int size() {
            return size;
        }

        void addFirst(int value) {
            requireSpace();
            front = Math.floorMod(front - 1, data.length);
            data[front] = value;
            size++;
        }

        void addLast(int value) {
            requireSpace();
            int rear = (int) (((long) front + size) % data.length);
            data[rear] = value;
            size++;
        }

        int removeFirst() {
            int value = getFirst();
            front = (front + 1) % data.length;
            size--;
            return value;
        }

        int removeLast() {
            int value = getLast();
            size--;
            return value;
        }

        int getFirst() {
            requireValue();
            return data[front];
        }

        int getLast() {
            requireValue();
            int last = (int) (((long) front + size - 1) % data.length);
            return data[last];
        }

        private void requireSpace() {
            if (size == data.length) throw new IllegalStateException("Deque is full");
        }

        private void requireValue() {
            if (size == 0) throw new NoSuchElementException("Deque is empty");
        }
    }

    public static void main(String[] args) {
        var deque = new IntDeque(3);
        deque.addLast(20);
        deque.addFirst(10);
        deque.addLast(30);
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.println(deque.getFirst());
        System.out.println(deque.size());
    }
}
