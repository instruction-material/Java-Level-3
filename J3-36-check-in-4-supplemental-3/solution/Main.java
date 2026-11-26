import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    record Arc(String from, String to) {}
    record Choice(long cost, List<String> route) {}

    static Optional<Choice> best(Map<Arc, Long> weights, List<List<String>> candidates) {
        Choice best = null;
        for (List<String> path : candidates) {
            if (path.isEmpty()) continue;
            long cost = 0;
            boolean valid = true;
            for (int i = 1; i < path.size(); i++) {
                Long edge = weights.get(new Arc(path.get(i - 1), path.get(i)));
                if (edge == null || edge < 0 || edge > Long.MAX_VALUE - cost) {
                    valid = false;
                    break;
                }
                cost += edge;
            }
            if (valid && (best == null || cost < best.cost())) best = new Choice(cost, List.copyOf(path));
        }
        return Optional.ofNullable(best);
    }

    public static void main(String[] args) {
        var weights = Map.of(new Arc("A", "B"), 2L, new Arc("B", "C"), 3L, new Arc("A", "C"), 8L);
        var choice = best(weights, List.of(List.of("C", "A"), List.of("A", "C"), List.of("A", "B", "C"))).orElseThrow();
        System.out.println(choice.cost());
        System.out.println(choice.route());
    }
}
