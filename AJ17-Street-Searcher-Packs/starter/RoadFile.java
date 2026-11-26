import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

class RoadFile {
    record Place(double latitude, double longitude) {}
    record Data(StreetGraph graph, Map<String, Place> places) {}

    static Data read(Path path) throws IOException {
        var graph = new StreetGraph();
        Map<String, Place> places = new HashMap<>();
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) continue;
                try {
                    addRow(graph, places, line);
                } catch (IllegalArgumentException error) {
                    throw new IllegalArgumentException("Line " + number + ": " + error.getMessage(), error);
                }
            }
        }
        return new Data(graph, Map.copyOf(places));
    }

    private static void addRow(StreetGraph graph, Map<String, Place> places, String line) {
        throw new UnsupportedOperationException("Parse and validate one local road record");
    }

    private static Place coordinates(String field) {
        String[] pair = field.split(",", -1);
        if (pair.length != 2) throw new IllegalArgumentException("Expected latitude,longitude");
        double latitude = Double.parseDouble(pair[0]);
        double longitude = Double.parseDouble(pair[1]);
        if (!Double.isFinite(latitude) || Math.abs(latitude) > 90
                || !Double.isFinite(longitude) || Math.abs(longitude) > 180) {
            throw new IllegalArgumentException("Coordinates outside latitude or longitude range");
        }
        return new Place(latitude, longitude);
    }

    private static void putPlace(Map<String, Place> places, String name, Place place) {
        Place previous = places.putIfAbsent(name, place);
        if (previous != null && !previous.equals(place)) {
            throw new IllegalArgumentException("Conflicting coordinates for " + name);
        }
    }
}
