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
        throw new UnsupportedOperationException("Reuse the route search while excluding closed stops");
    }

    private static Optional<Route> search(StreetGraph graph, String from, String to, Predicate<String> open) {
        if (!graph.contains(from) || !graph.contains(to)) throw new IllegalArgumentException("Unknown endpoint");
        if (!open.test(from) || !open.test(to)) return Optional.empty();
        throw new UnsupportedOperationException("Find the cheapest route through open stops");
    }

    private static Route collect(String to, double miles, Map<String, String> previous) {
        var stops = new ArrayDeque<String>();
        for (String at = to; at != null; at = previous.get(at)) stops.addFirst(at);
        return new Route(List.copyOf(stops), miles);
    }
}
