package legacy;
import api.*;
import java.util.Objects;
/** Working before design: the service repeatedly reaches into route data. */
public final class LegacyPlanner implements PlannerAPI {
    public LegacyPlanner() {}
    public int distance(RouteData route) {
        Objects.requireNonNull(route, "route");
        int distance = 0;
        for (int index = 1; index < route.stops().size(); index++) {
            Point before = route.stops().get(index - 1);
            Point after = route.stops().get(index);
            distance += Math.abs(after.x() - before.x()) + Math.abs(after.y() - before.y());
        }
        return distance;
    }
    public String summary(RouteData route) {
        Objects.requireNonNull(route, "route");
        return route.name()+"|stops="+route.stops().size()+"|distance="+distance(route);
    }
}
