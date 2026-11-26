import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class Main {
    record Road(String from, String to) {}
    record Finding(Road road, String reason) {}

    static List<Finding> audit(Map<Road, Integer> roads) {
        var ordered = new ArrayList<>(roads.keySet());
        ordered.sort(Comparator.comparing(Road::from).thenComparing(Road::to));
        var report = new ArrayList<Finding>();
        for (Road road : ordered) {
            Integer reverse = roads.get(new Road(road.to(), road.from()));
            if (reverse == null) report.add(new Finding(road, "one-way"));
            else if (!reverse.equals(roads.get(road))) report.add(new Finding(road, "different reverse cost"));
        }
        return List.copyOf(report);
    }

    public static void main(String[] args) {
        var roads = Map.of(new Road("A", "B"), 2, new Road("B", "A"), 4, new Road("B", "C"), 1);
        audit(roads).forEach(item -> System.out.println(item.road().from() + " -> " + item.road().to() + " " + item.reason()));
    }
}
