import java.util.LinkedHashMap;
import java.util.Map;

class StreetGraph {
    private final Map<String, Map<String, Double>> roads = new LinkedHashMap<>();

    void addPlace(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Expected a place name");
        roads.computeIfAbsent(name, ignored -> new LinkedHashMap<>());
    }

    void addRoad(String from, String to, double miles) {
        throw new UnsupportedOperationException("Store a directed road with its validated weight");
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
