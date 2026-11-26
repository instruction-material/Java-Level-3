import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    record Dispatch(String id, int count) {}

    static List<Dispatch> batch(List<String> arrivals) {
        var counts = new HashMap<String, Integer>();
        var queue = new ArrayDeque<String>();
        for (String id : arrivals) {
            if (counts.merge(id, 1, Integer::sum) == 1) queue.addLast(id);
        }
        var report = new ArrayList<Dispatch>();
        while (!queue.isEmpty()) {
            String id = queue.removeFirst();
            report.add(new Dispatch(id, counts.get(id)));
        }
        return report;
    }

    public static void main(String[] args) {
        System.out.println(batch(List.of("A2", "B1", "A2", "C3", "B1", "A2")));
    }
}
