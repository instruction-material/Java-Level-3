package extension;
import api.*;
import java.util.*;
public final class WaypointEdit {
    private WaypointEdit() {}
    public static RouteData replace(RouteData route, int index, Point replacement) {
        Objects.requireNonNull(route, "route");
        Objects.requireNonNull(replacement, "replacement");
        if (index < 0 || index >= route.stops().size()) throw new IndexOutOfBoundsException("index");
        List<Point> changed = new ArrayList<>(route.stops());
        changed.set(index, replacement);
        return new RouteData(route.name(), changed);
    }
}
