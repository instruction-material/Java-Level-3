import java.util.LinkedHashMap;
import java.util.Map;

class StreetGraph {
    private final Map<String, Map<String, Double>> roads = new LinkedHashMap<>();

    void addPlace(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Expected a place name");
        roads.computeIfAbsent(name, ignored -> new LinkedHashMap<>());
    }

    void addRoad(String from, String to, double miles) {
        if (!Double.isFinite(miles) || miles < 0) {
            throw new IllegalArgumentException("Expected finite nonnegative miles");
        }
        if (from.isBlank() || to.isBlank()) throw new IllegalArgumentException("Expected two place names");
        addPlace(from);
        addPlace(to);
        Double old = roads.get(from).putIfAbsent(to, miles);
        if (old != null && old != miles) throw new IllegalArgumentException("Conflicting road " + from + " to " + to);
    }

    boolean contains(String name) {
        return roads.containsKey(name);
    }

    int placeCount() {
        return roads.size();
    }

    int roadCount() {
        return roads.values().stream().mapToInt(Map::size).sum();
    }

    Map<String, Double> outgoing(String name) {
        Map<String, Double> edges = roads.get(name);
        if (edges == null) throw new IllegalArgumentException("Unknown place " + name);
        return Map.copyOf(edges);
    }
}
