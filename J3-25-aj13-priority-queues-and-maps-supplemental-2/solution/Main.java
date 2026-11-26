import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    record Job(String label, int urgency, long arrival) {}

    static List<String> schedule(List<Job> jobs) {
        var order = Comparator.comparingInt(Job::urgency).reversed().thenComparingLong(Job::arrival);
        var queue = new PriorityQueue<>(order);
        queue.addAll(jobs);
        var result = new ArrayList<String>(jobs.size());
        while (!queue.isEmpty()) {
            result.add(queue.remove().label());
        }
        return result;
    }

    public static void main(String[] args) {
        var jobs = List.of(new Job("Printer", 2, 1), new Job("Terminal", 4, 2),
                new Job("Router", 4, 3), new Job("Monitor", 1, 4));
        System.out.println(schedule(jobs));
    }
}
