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
        same(new Main.Event(LocalTime.of(9, 30), "CHECKOUT", "oak"), Main.parse("09:30| CHECKOUT |oak"));
        fails(IllegalArgumentException.class, () -> Main.parse("09:30|CHECKOUT| "));
        var input = Main.read(Path.of("events.txt"));
        same(List.of(2, 5), input.rejected());
        same(Map.of("CHECKOUT", 2L, "RETURN", 1L), Main.countStream(input.events()));
        same(Main.countLoop(input.events()), Main.countStream(input.events()));
        same(Optional.of(LocalTime.of(8, 5)), Main.first(input.events()));
        same(Optional.of(LocalTime.of(10, 20)), Main.last(input.events()));
        same(Optional.of("ALPHA"), Main.mostFrequent(Map.of("ZETA", 2L, "ALPHA", 2L)));
        same(Optional.empty(), Main.first(List.of()));
        same(Optional.empty(), Main.mostFrequent(Map.of()));
        System.out.println("Engineering checks passed");
    }
}
