import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    record Job(String label, int urgency, long arrival) {}

    static List<String> schedule(List<Job> jobs) {
        // TODO: Schedule urgent jobs first and keep arrival order on ties.
        throw new UnsupportedOperationException("Schedule urgent jobs first and keep arrival order on ties");
    }

    public static void main(String[] args) {
        var jobs = List.of(new Job("Printer", 2, 1), new Job("Terminal", 4, 2),
                new Job("Router", 4, 3), new Job("Monitor", 1, 4));
        System.out.println(schedule(jobs));
    }
}
