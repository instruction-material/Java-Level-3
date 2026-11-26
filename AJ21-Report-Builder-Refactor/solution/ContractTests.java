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
        same(Optional.of(new Main.Row("oak", 0)), Main.parse(" oak : 0 "));
        same(Optional.empty(), Main.parse("oak:-1"));
        same(Optional.empty(), Main.parse("oak:2:3"));
        same(new Main.Totals(Map.of("elm", 4L, "oak", 5L), 1), Main.aggregate(Main.sample()));
        same(List.of("elm=4", "oak=5", "skipped=1"), Main.report(Main.sample()));
        same(List.of("elm=4", "oak=5", "skipped=1"),
                Main.format(new Main.Totals(Map.of("oak", 5L, "elm", 4L), 1)));
        same(List.of("skipped=0"), Main.report(List.of()));
        same(List.of("oak=4294967294", "skipped=0"), Main.report(List.of("oak:2147483647", "oak:2147483647")));
        System.out.println("Engineering checks passed");
    }
}
