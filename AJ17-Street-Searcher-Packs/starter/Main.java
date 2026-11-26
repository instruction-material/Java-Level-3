import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"roads.txt", "Harbor", "Station"};
        }
        if (args.length != 3 && args.length != 4) {
            System.out.println("Usage: java Main file source destination [closed-stop]");
            return;
        }
        try {
            var graph = RoadFile.read(Path.of(args[0])).graph();
            var route = args.length == 3
                    ? RouteFinder.shortest(graph, args[1], args[2])
                    : RouteFinder.avoiding(graph, args[1], args[2], Set.of(args[3]));
            if (route.isEmpty()) {
                System.out.println("No route from " + args[1] + " to " + args[2]);
                return;
            }
            var result = route.orElseThrow();
            System.out.println(String.join(" -> ", result.stops()));
            System.out.printf(Locale.ROOT, "%.2f miles%n", result.miles());
        } catch (IOException | IllegalArgumentException error) {
            System.out.println("Road search error: " + error.getMessage());
        }
    }
}
