import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    static Map<String, Integer> replay(List<String> actions) {
        var counts = new TreeMap<String, Integer>();
        var history = new ArrayDeque<String>();
        for (String action : actions) {
            if (!action.equals("UNDO")) {
                history.push(action);
                counts.merge(action, 1, Integer::sum);
                continue;
            }
            String removed = history.poll();
            if (removed == null) continue;

            int count = counts.get(removed);
            if (count == 1) counts.remove(removed);
            else counts.put(removed, count - 1);
        }
        return counts;
    }

    public static void main(String[] args) {
        System.out.println(replay(List.of("UNDO", "lamp", "bulb", "lamp", "UNDO", "fan")));
    }
}
