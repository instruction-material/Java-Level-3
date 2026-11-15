import api.*;
import app.TripPlanner;
import legacy.LegacyPlanner;
import transfer.LongestLeg;
import extension.WaypointEdit;
import java.util.List;
public final class Main {
    private Main() {}
    public static RouteData sample() {
        return new RouteData("walk",List.of(new Point(0,0),new Point(3,0),new Point(3,-2),new Point(1,-2)));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        RouteData route=sample();
        switch(mode) {
            case "baseline" -> System.out.println(new LegacyPlanner().summary(route));
            case "core" -> System.out.println(new TripPlanner().summary(route));
            case "transfer" -> System.out.println(LongestLeg.measure(route));
            case "extension" -> System.out.println(new TripPlanner().summary(
                WaypointEdit.replace(route,1,new Point(3,4))));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
