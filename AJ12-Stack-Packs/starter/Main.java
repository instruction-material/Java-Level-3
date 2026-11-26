import java.util.NoSuchElementException;

public class Main {
    static final class IntStack {
        private final int[] data;
        private int size;

        IntStack(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
            data = new int[capacity];
        }

        int size() {
            return size;
        }

        void push(int value) {
            // TODO: Write a new top value.
            throw new UnsupportedOperationException("Write a new top value");
        }

        int pop() {
            // TODO: Return and remove the top value.
            throw new UnsupportedOperationException("Return and remove the top value");
        }

        int peek() {
            // TODO: Read the top without changing size.
            throw new UnsupportedOperationException("Read the top without changing size");
        }
    }

    public static void main(String[] args) {
        var stack = new IntStack(2);
        stack.push(7);
        stack.push(11);
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }
}
