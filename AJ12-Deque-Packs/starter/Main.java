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
            // TODO: Move the front backward and write the value.
            throw new UnsupportedOperationException("Move the front backward and write the value");
        }

        void addLast(int value) {
            // TODO: Write at the logical rear.
            throw new UnsupportedOperationException("Write at the logical rear");
        }

        int removeFirst() {
            // TODO: Remove the first value and advance the front.
            throw new UnsupportedOperationException("Remove the first value and advance the front");
        }

        int removeLast() {
            // TODO: Remove the last value without moving the front.
            throw new UnsupportedOperationException("Remove the last value without moving the front");
        }

        int getFirst() {
            // TODO: Read the first value.
            throw new UnsupportedOperationException("Read the first value");
        }

        int getLast() {
            // TODO: Read the last occupied slot.
            throw new UnsupportedOperationException("Read the last occupied slot");
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
