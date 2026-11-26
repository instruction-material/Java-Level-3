import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Predicate;

class RouteFinder {
    record Route(List<String> stops, double miles) {}
    private record Candidate(String name, double miles) {}

    static Optional<Route> shortest(StreetGraph graph, String from, String to) {
        return search(graph, from, to, ignored -> true);
    }

    static Optional<Route> avoiding(StreetGraph graph, String from, String to, Set<String> closed) {
        for (String stop : closed) {
            if (!graph.contains(stop)) throw new IllegalArgumentException("Unknown closure " + stop);
        }
        return search(graph, from, to, stop -> !closed.contains(stop));
    }

    private static Optional<Route> search(StreetGraph graph, String from, String to, Predicate<String> open) {
        if (!graph.contains(from) || !graph.contains(to)) throw new IllegalArgumentException("Unknown endpoint");
        if (!open.test(from) || !open.test(to)) return Optional.empty();
        var best = new HashMap<String, Double>();
        var previous = new HashMap<String, String>();
        var pending = new PriorityQueue<>(Comparator.comparingDouble(Candidate::miles).thenComparing(Candidate::name));
        best.put(from, 0.0);
        pending.add(new Candidate(from, 0));
        while (!pending.isEmpty()) {
            Candidate current = pending.remove();
            if (current.miles() != best.get(current.name())) continue;
            if (current.name().equals(to)) return Optional.of(collect(to, current.miles(), previous));
            for (var edge : graph.outgoing(current.name()).entrySet()) {
                String next = edge.getKey();
                double miles = current.miles() + edge.getValue();
                if (open.test(next) && miles < best.getOrDefault(next, Double.POSITIVE_INFINITY)) {
                    best.put(next, miles);
                    previous.put(next, current.name());
                    pending.add(new Candidate(next, miles));
                }
            }
        }
        return Optional.empty();
    }

    private static Route collect(String to, double miles, Map<String, String> previous) {
        var stops = new ArrayDeque<String>();
        for (String at = to; at != null; at = previous.get(at)) stops.addFirst(at);
        return new Route(List.copyOf(stops), miles);
    }
}
