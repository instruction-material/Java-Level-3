import api.*;
import app.TripPlanner;
import java.util.List;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        PlannerAPI planner=new TripPlanner();
        RouteData input=new RouteData("edge",List.of(new Point(-100,100),new Point(100,-100)));
        if(planner.distance(input)!=400)throw new AssertionError("distance");
        if(!planner.summary(input).equals("edge|stops=2|distance=400"))throw new AssertionError("summary");
        System.out.println("compatibility passed");
    }
}
