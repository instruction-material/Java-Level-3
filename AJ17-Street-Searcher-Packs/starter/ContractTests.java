import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    static void rejects(String reason, Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException error) {
            if (error.getMessage().contains(reason)) return;
            throw error;
        }
        throw new AssertionError("Expected " + reason);
    }

    static StreetGraph example() {
        var graph = new StreetGraph();
        graph.addRoad("Harbor", "Market", 2.5);
        graph.addRoad("Harbor", "Station", 8);
        graph.addRoad("Market", "Station", 2.5);
        graph.addRoad("Market", "Harbor", 0);
        graph.addRoad("Market", "Library", 1);
        graph.addPlace("Garden");
        return graph;
    }

    static void graph() {
        var graph = example();
        same(5, graph.placeCount());
        same(5, graph.roadCount());
        same(2.5, graph.outgoing("Harbor").get("Market"));
        same(0.0, graph.outgoing("Market").get("Harbor"));
        same(Set.of(), graph.outgoing("Garden").keySet());
        same(false, graph.outgoing("Station").containsKey("Market"));
        graph.addRoad("Harbor", "Market", 2.5);
        same(5, graph.roadCount());
        rejects("Conflicting road", () -> graph.addRoad("Harbor", "Market", 3));
        rejects("nonnegative", () -> graph.addRoad("New", "Here", -1));
        rejects("nonnegative", () -> graph.addRoad("New", "Here", Double.NaN));
        rejects("place name", () -> graph.addRoad("New", "", 0));
        same(false, graph.contains("New"));
        same(2.5, graph.outgoing("Harbor").get("Market"));
    }

    static void load() throws IOException {
        var path = Files.createTempFile("roads-test-", ".txt");
        try {
            Files.writeString(path, "# fixture\n\n1,1   1,2  2.5 Harbor--Market\n1,2\t1,3 0 Market--Station\n");
            var data = RoadFile.read(path);
            same(3, data.places().size());
            same(new RoadFile.Place(1, 2), data.places().get("Market"));
            same(0.0, data.graph().outgoing("Market").get("Station"));
            rejectFile(path, "1,1 1,2 2 A--B\nbad\n", "Line 2");
            rejectFile(path, "91,1 1,2 2 A--B\n", "outside latitude");
            rejectFile(path, "1,1 1,2 -1 A--B\n", "nonnegative");
            rejectFile(path, "1,1 1,2 2 A--B\n1,2 1,3 3 A--C\n", "Conflicting coordinates for A");
            Files.writeString(path, "\n# empty graph\n");
            same(0, RoadFile.read(path).graph().placeCount());
        } finally {
            Files.deleteIfExists(path);
        }
    }

    static void rejectFile(Path path, String contents, String reason) throws IOException {
        Files.writeString(path, contents);
        try {
            RoadFile.read(path);
        } catch (IllegalArgumentException error) {
            if (error.getMessage().contains(reason)) return;
            throw error;
        }
        throw new AssertionError("Expected invalid road file: " + reason);
    }

    static void route() {
        var graph = example();
        same(new RouteFinder.Route(List.of("Harbor", "Market", "Station"), 5),
                RouteFinder.shortest(graph, "Harbor", "Station").orElseThrow());
        same(new RouteFinder.Route(List.of("Station"), 0),
                RouteFinder.shortest(graph, "Station", "Station").orElseThrow());
        same(Optional.empty(), RouteFinder.shortest(graph, "Harbor", "Garden"));
        same(Optional.empty(), RouteFinder.shortest(graph, "Station", "Harbor"));
        rejects("Unknown endpoint", () -> RouteFinder.shortest(graph, "Unknown", "Harbor"));
        graph.addRoad("Harbor", "Annex", 3);
        graph.addRoad("Annex", "Station", 2);
        var tied = RouteFinder.shortest(graph, "Harbor", "Station").orElseThrow();
        same(5.0, tied.miles());
        double total = 0;
        for (int i = 1; i < tied.stops().size(); i++) {
            total += graph.outgoing(tied.stops().get(i - 1)).get(tied.stops().get(i));
        }
        same(tied.miles(), total);
    }

    static void avoid() {
        var graph = example();
        same(new RouteFinder.Route(List.of("Harbor", "Station"), 8),
                RouteFinder.avoiding(graph, "Harbor", "Station", Set.of("Market")).orElseThrow());
        same(Optional.empty(), RouteFinder.avoiding(graph, "Harbor", "Station", Set.of("Station")));
        same(Optional.empty(), RouteFinder.avoiding(graph, "Harbor", "Library", Set.of("Market")));
        rejects("Unknown closure", () -> RouteFinder.avoiding(graph, "Harbor", "Station", Set.of("Unknown")));
        same(5.0, RouteFinder.shortest(graph, "Harbor", "Station").orElseThrow().miles());
    }

    public static void main(String[] args) throws IOException {
        String mode = args.length == 0 ? "core" : args[0];
        switch (mode) {
            case "graph" -> graph();
            case "load" -> load();
            case "route" -> route();
            case "avoid" -> avoid();
            case "core" -> {
                graph();
                load();
                route();
            }
            default -> throw new IllegalArgumentException("Use graph, load, route, or avoid");
        }
        System.out.println(mode + " checks passed");
    }
}
