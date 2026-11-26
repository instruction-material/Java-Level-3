import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

public class ContractTests {
    private static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var first = new Main.Job("Terminal", 4, Long.MIN_VALUE);
        var second = new Main.Job("Router", 4, Long.MAX_VALUE);
        var low = new Main.Job("Monitor", Integer.MIN_VALUE, 4);
        var high = new Main.Job("Disk", Integer.MAX_VALUE, 3);
        var jobs = new java.util.ArrayList<>(List.of(low, second, first, high));
        expect(List.of("Disk", "Terminal", "Router", "Monitor"), Main.schedule(jobs));
        expect(List.of(low, second, first, high), jobs);
        expect(List.of(), Main.schedule(List.of()));

        System.out.println("Priority queue and map checks passed");
    }
}
