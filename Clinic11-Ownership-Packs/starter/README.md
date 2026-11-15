# Refactoring Clinic 11: Ownership and feature envy

The working TripPlanner computes route distance by repeatedly reading another object's name and stop list. Move the route's decisions beside its owned state while keeping the client-facing PlannerAPI unchanged. This narrow clinic concentrates on ownership and client impact; the capstone requires a larger diagnosis and sequence.

## Build and characterize

Use JDK 21 from this project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -ea -cp out ContractChecks core
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Both Main modes print exactly:

    walk|stops=4|distance=7

The output ends with newline. The compatibility client prints "compatibility passed". The demo visits (0,0), (3,0), (3,-2), (1,-2). Its three legs have lengths 3, 2 and 2. These are open grid routes: each leg length is abs(nextX-currentX)+abs(nextY-currentY); there is no return to the start. Empty and single-stop routes have distance zero. No program reads standard input or contacts a file service, database or network; the application operates on in-memory values and prints to the console.

The starter is already behaviorally correct. Baseline checks characterize the frozen LegacyPlanner and the candidate TripPlanner; core checks characterize only the candidate. Passing results protect behavior, while source changes and a step log establish whether ownership improved. Keep Main, CompatibilityClient, ContractChecks, PlannerAPI, Point, RouteData and LegacyPlanner unchanged. Record work in REFACTORING.md.

## Required project: move decisions with their data

Trace summary(route) through name, stop count and distance before editing. Mark each access to RouteData and explain why the service knows so much about route representation. Contrast a method that mostly uses another object's data with a useful service that coordinates several independent objects: data access alone is insufficient to diagnose feature envy.

First extract a private leg(Point,Point) method in TripPlanner without changing its loop. Compile and run baseline/core. Then introduce an owned route object, give it the route name and an immutable stop list, and move distance with that state. Move summary after its inputs are characterized. Keep new TripPlanner(), distance(RouteData) and summary(RouteData) available. Add implementation files to sources.txt.

The reference's package-private RoutePlan owns name and stops and computes its own distance and summary. TripPlanner remains the public facade that validates the route and constructs that owned object. RouteData stays a frozen input value at the client boundary. The client neither selects a RoutePlan implementation nor reaches into its state. A separate calculator that keeps fetching every point from an unrelated object can leave the original ownership problem in place.

The contract is:

- Point accepts x and y from -100 through 100 inclusive. Invalid x throws IllegalArgumentException("x") before y validation; invalid y throws IllegalArgumentException("y").
- RouteData validates name before stops. Null name throws NullPointerException("name"). Blank names, names longer than 24 Java char units, and pipe/carriage-return/newline characters throw IllegalArgumentException("name"). Other accepted characters and surrounding spaces are preserved exactly.
- Null stop list throws NullPointerException("stops"). Null members are rejected by List.copyOf. There can be zero through eight stops; more throws IllegalArgumentException("stops"). The list is copied and unmodifiable. Point values are immutable.
- distance and summary validate a non-null route with NullPointerException("route"). They leave its data unchanged and use consecutive stops in their original order. Maximum possible distance is 2800, so the defined domain fits int.
- summary returns "NAME|stops=N|distance=D", with no newline inside the returned string. Main adds the displayed newline. Different planner objects produce the same result for the same value and share no mutable route state.

Test a null route, an empty route, repeated points, a negative-coordinate route and the opposing-corner route (-100,100) to (100,-100), whose distance is 400. A shortcut abs(dx+dy) fails the last case because opposite signs cancel.

Record the old call path, the moved responsibility, the new owner, unchanged public calls and exact before/after output. Preserve actual source steps and passing results. Explain one benefit, one tradeoff of constructing an owned value at this boundary, and a change this clinic deliberately postpones. A route object is justified by cohesive state and decisions, not by increasing the number of classes.

## Optional continuation: inspect the ownership boundary

Keep the core project and its identity. Add a separate client that summarizes an empty route and then the same unusual route twice. Predict its results and check that the original input list stays unchanged. Compare a solution that owns the immutable points with a helper that repeatedly reaches through RouteData. Explain whether a new coordinate representation could be confined behind the owned route. This continuation adds evidence to the core refactor, rather than a separate required algorithm.

## Optional transfer: longest leg

Complete transfer.LongestLeg.measure(RouteData), independently of the core. Validate null with NullPointerException("route"). Return the maximum consecutive Manhattan leg length; return zero for zero or one stop. Preserve the route and every point in order. There is no closing leg. The sample's result is 3, distinct from its total distance 7.

    java -ea -cp out ContractChecks transfer
    java -da -cp out ContractChecks transfer
    java -cp out Main transfer

Explain whether this new query belongs on an owned route or in a stateless operation over an immutable input value. The reference uses the latter for this independent exercise; compare the tradeoff with the core's cohesive route behavior. Retain a route where longest leg differs from total distance, an empty route and an opposing-sign case. The frozen checks cover values and purity; the source explanation addresses ownership.

## Optional extension: waypoint replacement preview

Complete extension.WaypointEdit.replace(route,index,replacement), independently of core and transfer. This introduces a new operation with its own contract. Validate route first with NullPointerException("route"), then replacement with NullPointerException("replacement"), then the index. An index outside 0 through size-1 throws IndexOutOfBoundsException("index"). Thus an empty route has no valid replacement index.

Return a new RouteData with the exact original name and stop count, replacing only the selected position. Preserve the input and all other positions. Its returned stop list is unmodifiable. Coordinates follow the frozen Point contract. Replacing the sample's second point with (3,4) changes the total to 15: the new leg lengths are 7, 6 and 2.

    java -ea -cp out ContractChecks extension
    java -da -cp out ContractChecks extension
    java -cp out Main extension

Demonstrate the first and last indices, a rejected index and input retained after an edit. Keep this feature separate from the core's behavior-preserving moves.

## Separate client and review

After the chosen implementation is complete, write Probe.java:

    import api.*;
    import app.TripPlanner;
    import java.util.List;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            PlannerAPI planner = new TripPlanner();
            RouteData route = new RouteData("edge",
                List.of(new Point(-100,100), new Point(100,-100)));
            if (planner.distance(route) != 400)
                throw new AssertionError("distance");
            if (!planner.summary(route).equals("edge|stops=2|distance=400"))
                throw new AssertionError("summary");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. This external client uses the original facade without knowing which class owns the moved behavior.

After all three projects are complete, ContractChecks all runs their combined checks. Its explicit failures execute under -ea and -da. Keep learner-written checks in separate files. A final review includes the ownership argument, exact behavior evidence, a small step log and a limitation; a green result alone cannot evaluate that explanation.
