import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class Main {
    record Road(String from, String to) {}
    record Finding(Road road, String reason) {}

    static List<Finding> audit(Map<Road, Integer> roads) {
        // TODO: Classify each directed road by its reverse.
        throw new UnsupportedOperationException("Classify each directed road by its reverse");
    }

    public static void main(String[] args) {
        var roads = Map.of(new Road("A", "B"), 2, new Road("B", "A"), 4, new Road("B", "C"), 1);
        audit(roads).forEach(item -> System.out.println(item.road().from() + " -> " + item.road().to() + " " + item.reason()));
    }
}
