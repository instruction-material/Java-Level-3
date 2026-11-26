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
        same(new Main.Row("a", 0, Main.Status.READY), Main.parse(" a , 0, READY "));
        fails(IllegalArgumentException.class, () -> Main.parse("x,4,LOST"));
        fails(IllegalArgumentException.class, () -> Main.parse("x,2147483648,READY"));
        fails(IllegalArgumentException.class, () -> Main.parse(",1,READY"));
        var original = new Main.Row("existing", 1, Main.Status.READY);
        var old = new ArrayList<>(List.of(original));
        var result = Main.importRows(old, Path.of("records.csv"));
        same(List.of(original, new Main.Row("oak", 3, Main.Status.READY),
                new Main.Row("cedar", 0, Main.Status.WAITING)), result.accepted());
        same(List.of(new Main.Rejection(2, "negative count"), new Main.Rejection(3, "duplicate id"),
                new Main.Rejection(5, "duplicate id"), new Main.Rejection(6, "invalid status")), result.rejected());
        same(List.of(original), old);
        fails(UnsupportedOperationException.class, () -> result.accepted().clear());
        System.out.println("Engineering checks passed");
    }
}
