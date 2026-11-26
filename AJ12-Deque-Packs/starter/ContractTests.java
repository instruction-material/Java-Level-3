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
        var deque = new Main.IntDeque(3);
        deque.addFirst(10);
        deque.addLast(20);
        deque.addFirst(0);
        rejects(IllegalStateException.class, () -> deque.addFirst(99));
        rejects(IllegalStateException.class, () -> deque.addLast(99));
        expect(0, deque.getFirst());
        expect(20, deque.getLast());
        expect(20, deque.removeLast());
        expect(0, deque.removeFirst());
        expect(10, deque.removeFirst());
        expect(0, deque.size());
        rejects(NoSuchElementException.class, deque::removeFirst);
        rejects(NoSuchElementException.class, deque::removeLast);
        rejects(NoSuchElementException.class, deque::getFirst);
        rejects(NoSuchElementException.class, deque::getLast);
        rejects(IllegalArgumentException.class, () -> new Main.IntDeque(-1));
        var random = new java.util.SplittableRandom(34);
        for (int capacity : new int[]{1, 2, 7}) {
            var actual = new Main.IntDeque(capacity);
            var expected = new java.util.ArrayDeque<Integer>();
            for (int step = 0; step < 1024; step++) {
                int operation = random.nextInt(4);
                int value = random.nextInt();
                if (operation == 0) {
                    if (expected.size() == capacity) {
                        rejects(IllegalStateException.class, () -> actual.addFirst(value));
                    } else {
                        expected.addFirst(value);
                        actual.addFirst(value);
                    }
                } else if (operation == 1) {
                    if (expected.size() == capacity) {
                        rejects(IllegalStateException.class, () -> actual.addLast(value));
                    } else {
                        expected.addLast(value);
                        actual.addLast(value);
                    }
                } else if (operation == 2) {
                    if (expected.isEmpty()) rejects(NoSuchElementException.class, actual::removeFirst);
                    else expect(expected.removeFirst(), actual.removeFirst());
                } else {
                    if (expected.isEmpty()) rejects(NoSuchElementException.class, actual::removeLast);
                    else expect(expected.removeLast(), actual.removeLast());
                }
                expect(expected.size(), actual.size());
                if (!expected.isEmpty()) {
                    expect(expected.getFirst(), actual.getFirst());
                    expect(expected.getLast(), actual.getLast());
                }
            }
        }
        System.out.println("Stack and queue checks passed");
    }
}
