import java.util.NoSuchElementException;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Class<? extends RuntimeException> type, Runnable operation) {
        try {
            operation.run();
        } catch (RuntimeException error) {
            if (type.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args) {
        var stack = new Main.IntStack(2);
        stack.push(7);
        stack.push(11);
        rejects(IllegalStateException.class, () -> stack.push(99));
        expect(2, stack.size());
        expect(11, stack.peek());
        expect(2, stack.size());
        expect(11, stack.pop());
        stack.push(-4);
        expect(-4, stack.pop());
        expect(7, stack.pop());
        expect(0, stack.size());
        rejects(NoSuchElementException.class, stack::pop);
        rejects(NoSuchElementException.class, stack::peek);
        rejects(IllegalArgumentException.class, () -> new Main.IntStack(0));
        var single = new Main.IntStack(1);
        single.push(Integer.MIN_VALUE);
        expect(Integer.MIN_VALUE, single.pop());
        System.out.println("Stack and queue checks passed");
    }
}
