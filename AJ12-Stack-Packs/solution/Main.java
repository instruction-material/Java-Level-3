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
            if (size == data.length) throw new IllegalStateException("Stack is full");
            data[size] = value;
            size++;
        }

        int pop() {
            int value = peek();
            size--;
            return value;
        }

        int peek() {
            if (size == 0) throw new NoSuchElementException("Stack is empty");
            return data[size - 1];
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
