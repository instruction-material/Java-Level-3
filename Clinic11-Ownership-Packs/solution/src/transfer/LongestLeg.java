package transfer;
import api.*;
import java.util.Objects;
import java.util.List;
public final class LongestLeg {
    private LongestLeg() {}
    public static int measure(RouteData route) {
        Objects.requireNonNull(route, "route");
        List<Point> stops = route.stops();
        int longest = 0;
        for (int index = 1; index < stops.size(); index++) {
            Point before = stops.get(index - 1), after = stops.get(index);
            int leg = Math.abs(after.x()-before.x()) + Math.abs(after.y()-before.y());
            longest = Math.max(longest, leg);
        }
        return longest;
    }
}
