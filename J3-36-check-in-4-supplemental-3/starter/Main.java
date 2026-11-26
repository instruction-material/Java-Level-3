import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    record Arc(String from, String to) {}
    record Choice(long cost, List<String> route) {}

    static Optional<Choice> best(Map<Arc, Long> weights, List<List<String>> candidates) {
        // TODO: Check each proposed route and choose the cheapest valid candidate.
        throw new UnsupportedOperationException("Check each proposed route and choose the cheapest valid candidate");
    }

    public static void main(String[] args) {
        var weights = Map.of(new Arc("A", "B"), 2L, new Arc("B", "C"), 3L, new Arc("A", "C"), 8L);
        var choice = best(weights, List.of(List.of("C", "A"), List.of("A", "C"), List.of("A", "B", "C"))).orElseThrow();
        System.out.println(choice.cost());
        System.out.println(choice.route());
    }
}
