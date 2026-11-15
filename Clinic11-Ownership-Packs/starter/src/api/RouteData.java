package api;
import java.util.List;
import java.util.Objects;
/** Immutable input value; the owned route in the candidate carries its behavior. */
public record RouteData(String name, List<Point> stops) {
    public RouteData {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || name.length() > 24 || name.indexOf('|') >= 0
                || name.indexOf('\r') >= 0 || name.indexOf('\n') >= 0)
            throw new IllegalArgumentException("name");
        stops = List.copyOf(Objects.requireNonNull(stops, "stops"));
        if (stops.size() > 8) throw new IllegalArgumentException("stops");
    }
}
