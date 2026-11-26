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
        var queue = new Main.IntQueue(3);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        rejects(IllegalStateException.class, () -> queue.enqueue(99));
        expect(3, queue.size());
        expect(10, queue.peek());
        expect(10, queue.dequeue());
        queue.enqueue(40);
        expect(3, queue.size());
        expect(20, queue.dequeue());
        expect(30, queue.dequeue());
        expect(40, queue.dequeue());
        rejects(NoSuchElementException.class, queue::peek);
        rejects(NoSuchElementException.class, queue::dequeue);
        rejects(IllegalArgumentException.class, () -> new Main.IntQueue(0));
        var random = new java.util.SplittableRandom(12);
        for (int capacity : new int[]{1, 2, 7}) {
            var actual = new Main.IntQueue(capacity);
            var expected = new java.util.ArrayDeque<Integer>();
            for (int step = 0; step < 1024; step++) {
                if (random.nextBoolean()) {
                    int value = random.nextInt(-20, 21);
                    if (expected.size() == capacity) {
                        rejects(IllegalStateException.class, () -> actual.enqueue(value));
                    } else {
                        expected.addLast(value);
                        actual.enqueue(value);
                    }
                } else if (expected.isEmpty()) {
                    rejects(NoSuchElementException.class, actual::dequeue);
                } else {
                    expect(expected.removeFirst(), actual.dequeue());
                }
                expect(expected.size(), actual.size());
                if (!expected.isEmpty()) expect(expected.getFirst(), actual.peek());
            }
        }
        System.out.println("Stack and queue checks passed");
    }
}
