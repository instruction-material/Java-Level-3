import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

public class Pathfinder {
    record Route(List<String> stops, double distance) {}
    private record Candidate(String name, double distance) {}

    private final WeightedGraph<String, Double> graph;
    private final Map<String, Location> locations;

    public Pathfinder(WeightedGraph<String, Double> graph,
                      HashMap<String, Location> locations) {
        this.graph = graph;
        this.locations = locations;
    }

    public void findShortestPath(Location start, Location end) {
        Optional<Route> result = shortestRoute(start.getName(), end.getName());
        if (result.isEmpty()) {
            System.out.printf("%nNo route from %s to %s.%n%n", start.getName(), end.getName());
            return;
        }

        Route route = result.get();
        System.out.printf("%nTotal Distance: %.2f miles%n", route.distance());
        List<String> stops = route.stops();
        for (int i = 1; i < stops.size(); i++) {
            String from = stops.get(i - 1);
            String to = stops.get(i);
            System.out.printf("%s --> %s(%.2f miles)%n", from, to, graph.getEdges(from).get(to));
        }
        System.out.println();
    }

    Optional<Route> shortestRoute(String start, String target) {
        if (!locations.containsKey(start) || !locations.containsKey(target)) {
            return Optional.empty();
        }
        var distance = new HashMap<String, Double>();
        var previous = new HashMap<String, String>();
        var frontier = new PriorityQueue<>(
                Comparator.comparingDouble(Candidate::distance).thenComparing(Candidate::name));
        distance.put(start, 0.0);
        frontier.add(new Candidate(start, 0.0));
        while (!frontier.isEmpty()) {
            Candidate current = frontier.remove();
            if (current.distance() != distance.get(current.name())) continue;
            if (current.name().equals(target)) {
                return Optional.of(reconstruct(target, current.distance(), previous));
            }
            for (var road : graph.getEdges(current.name()).entrySet()) {
                String next = road.getKey();
                double cost = current.distance() + road.getValue();
                if (cost < distance.getOrDefault(next, Double.POSITIVE_INFINITY)) {
                    distance.put(next, cost);
                    previous.put(next, current.name());
                    frontier.add(new Candidate(next, cost));
                }
            }
        }
        return Optional.empty();
    }

    private static Route reconstruct(String target, double distance, Map<String, String> previous) {
        var route = new ArrayDeque<String>();
        for (String current = target; current != null; current = previous.get(current)) {
            route.addFirst(current);
        }
        return new Route(List.copyOf(route), distance);
    }
}
