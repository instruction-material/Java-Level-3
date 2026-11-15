package app;
import api.Point;
import java.util.List;
/** One route owns immutable points and the decisions based on those points. */
final class RoutePlan {
    private final String name;
    private final List<Point> stops;
    RoutePlan(String name, List<Point> stops) {
        this.name = name;
        this.stops = List.copyOf(stops);
    }
    int distance() {
        int total = 0;
        for (int index = 1; index < stops.size(); index++) {
            Point before = stops.get(index - 1);
            Point after = stops.get(index);
            total += Math.abs(after.x() - before.x()) + Math.abs(after.y() - before.y());
        }
        return total;
    }
    String summary() { return name+"|stops="+stops.size()+"|distance="+distance(); }
}
