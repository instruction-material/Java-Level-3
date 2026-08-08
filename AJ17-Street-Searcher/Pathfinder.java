import java.util.*;

public class Pathfinder {

    public static WeightedGraph<String, Double> graph;
    public static HashMap<String, Location> locations;
    public static PriorityQueue<Location> unvisited;

    public Pathfinder(WeightedGraph<String, Double> graph,
                      HashMap<String, Location> locations) {
        this.graph = graph;
        this.locations = locations;
    }
    public void findShortestPath(Location start, Location end) {

        //set every vertex's "distance from the source" to infinity
        //set the source's distance to 0
        for (Location location : locations.values()) {
            location.setDistance(Double.MAX_VALUE);
            location.setPrevious(null);
        }
        start.setDistance(0.0);

        //fill the priority queue
        unvisited = new PriorityQueue<>();
        for (Location location : locations.values()) {
            unvisited.add(location);
        }

        //run the algorithm
        Location nearest;
        while (unvisited.contains(end)) {
            nearest = unvisited.poll();
            updateNeighbors(nearest);
        }

        //print the verticies
        System.out.printf("\nTotal Distance: %.2f miles\n",
                          getTotalDistance(end));
        printPath(end);
        System.out.println();
    }
    private void updateNeighbors(Location nearest) {

        //get the roads to all neighboring locations
        HashMap<String, Double> roads;
        roads = graph.getEdges(nearest.getName());

        //update the distance of neighboring locations
        for (String locationName : roads.keySet()) {
            Location location = locations.get(locationName);
            if (unvisited.contains(location)) {
                double current = location.getDistance();
                double alternate =
                    nearest.getDistance() + roads.get(locationName);
                //if the alternate is less than the current distance
                if (alternate < current) {
                    //replace the current with the alternate
                    location.setDistance(alternate);
                    location.setPrevious(nearest);
                    //remove and add the neighboring location...
                    //...to update it's priority in the queue
                    unvisited.remove(location);
                    unvisited.add(location);
                }
            }
        }
    }
    private double getTotalDistance(Location location) {
        Location previous = location.getPrevious();

        //base case
        if (previous == null)
            return 0;

        //recursive case
        String name = location.getName();
        String previousName = previous.getName();
        double distance = graph.getEdges(previousName).get(name);
        return distance + getTotalDistance(previous);
    }
    private void printPath(Location location) {
        Location previous = location.getPrevious();

        //base case
        if (previous == null)
            return;

        //recursive case
        printPath(previous);

        String name = location.getName();
        String previousName = previous.getName();
        System.out.print(previousName + " --> " + name);

        double distance = graph.getEdges(previousName).get(name);
        System.out.printf("(%.2f miles)\n", distance);
    }
}
