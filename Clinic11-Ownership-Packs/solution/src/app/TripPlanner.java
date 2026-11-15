package app;
import api.*;
import java.util.Objects;
/** Stable client facade; route state and decisions belong together. */
public final class TripPlanner implements PlannerAPI {
    public TripPlanner() {}
    public int distance(RouteData route) { return owned(route).distance(); }
    public String summary(RouteData route) { return owned(route).summary(); }
    private RoutePlan owned(RouteData route) {
        Objects.requireNonNull(route, "route");
        return new RoutePlan(route.name(), route.stops());
    }
}
