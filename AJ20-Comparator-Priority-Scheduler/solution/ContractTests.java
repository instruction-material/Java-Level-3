import java.util.*;
import java.nio.file.*;
import java.time.*;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void fails(Class<? extends RuntimeException> type, Runnable change) {
        try {
            change.run();
        } catch (RuntimeException error) {
            if (type.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        var tasks = new ArrayList<>(Main.example());
        same(List.of("A", "B", "C"), Main.fromQueue(tasks, Main.comparator(Main.Policy.DEADLINE)));
        same(List.of("B", "A", "C"), Main.fromQueue(tasks, Main.comparator(Main.Policy.IMPORTANCE)));
        same(List.of("C", "B", "A"), Main.fromQueue(tasks, Main.comparator(Main.Policy.DURATION)));
        for (var policy : Main.Policy.values()) {
            same(Main.fromSortedList(tasks, Main.comparator(policy)), Main.fromQueue(tasks, Main.comparator(policy)));
        }
        same(List.of("C", "B", "A"), Main.fromQueue(tasks, Comparator.comparing(Main.Task::id).reversed()));
        same(Main.example(), tasks);
        same(List.of(), Main.fromQueue(List.of(), Main.comparator(Main.Policy.DEADLINE)));
        fails(IllegalArgumentException.class, () -> new Main.Task("zero", 1, 0, 0));
        System.out.println("Engineering checks passed");
    }
}
