import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    private static final double MAX_DISTANCE = 1e18;
    private static Map<String, Vertex<String>> vertices = new HashMap<>();
    private static SparseGraph<String, String> graph = new SparseGraph<>();
    private static HashMap<String, String> touristAttractions = new HashMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nWelcome to the Street Searcher "
                         + "algorithm!\nPlease enter a file name: ");
        String fileName = scanner.nextLine();

        int numRoads = 0;
        try {
            numRoads = loadNetwork(fileName);
            System.out.println("\nNetwork Loaded!");
            System.out.println("Loaded " + numRoads + " roads");
            System.out.println("Loaded " + vertices.size() + " endpoints");

            // Create a hashmap to store names & coordinates
            touristAttractions = loadHashMap(fileName, numRoads);
            System.out.println("\nLOCATIONS:");
            for (String attraction : touristAttractions.keySet()) {
                System.out.println(attraction);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file " + fileName);
            return;
        }
        //System.out.println(graph);

        while (true) {
            try {
                System.out.print("\nEnter a starting location: ");
                String startName = scanner.nextLine();
                String startCoordinate = touristAttractions.get(startName);
                checkValidEndpoint(startCoordinate);

                System.out.print("Enter an ending location: ");
                String endName = scanner.nextLine();
                String endCoordinate = touristAttractions.get(endName);
                checkValidEndpoint(endCoordinate);

                findShortestPath(startCoordinate, endCoordinate);

            } catch (IllegalArgumentException e) {
                System.err.println("Invalid location. Try again.");
            }
        }
    }

    //LOAD NETWORK
    //LOAD MAP
    //SHORTEST PATH
    //PRINT THE SOLUTION

    // Load network from fileName, returns number of roads
    private static int loadNetwork(String fileName)
        throws FileNotFoundException {

        int numRoads = 0;

        // Read in from file fileName
        Scanner input = new Scanner(new FileInputStream(new File(fileName)));
        while (input.hasNext()) {
            // Parse the line in to <end1> <end2> <road-distance> <road-name>
            String[] tokens = input.nextLine().split(" ");
            String fromName = tokens[0];
            String toName = tokens[1];
            double roadDistance = Double.parseDouble(tokens[2]);
            String roadName = tokens[3];

            // Get the from and to endpoints, adding if necessary
            Vertex<String> from = addLocation(fromName);
            Vertex<String> to = addLocation(toName);

            // Add the road to the network - We assume all roads are two-way and
            // ignore if we've already added the road as a reverse of another
            try {
                Edge<String> road = graph.insert(from, to, roadName);
                Edge<String> backwardsRoad = graph.insert(to, from, roadName);
                numRoads += 2;

                // Label each road with it's weight
                graph.label(road, roadDistance);
                graph.label(backwardsRoad, roadDistance);

            } catch (InsertionException ignored) {
                // Nothing to do.
            }
        }

        return numRoads;
    }

    // Add an endpoint to the network if it is a new endpoint
    private static Vertex<String> addLocation(String name) {
        if (!vertices.containsKey(name)) {
            Vertex<String> v = graph.insert(name);
            vertices.put(name, v);
            return v;
        }
        return vertices.get(name);
    }

    /** Method to load each point in the filename into a hashmap consisting of place names as keys and coordinates (latitude,longitude pairs) as values
  */
    private static HashMap loadHashMap(String fileName, int numRoads)
        throws FileNotFoundException {

        HashMap<String, String> map = new HashMap<>();

        //int numLoaded = 0;
        Scanner in = new Scanner(new FileInputStream(new File(fileName)));

        while (in.hasNext()) {

            // Parse the line in to <end1> <end2> <road-distance> <road-name>
            String[] tokens = in.nextLine().split(" ");
            String fromName = tokens[0];

            String toName = tokens[1];

            String[] locations = tokens[3].split("--");

            String location1 = locations[0];
            String location2 = locations[1];

            if (!(map.containsKey(location1))) {
                map.put(location1, fromName);
            }
            if (!(map.containsKey(location2))) {
                map.put(location2, toName);
            }
        }

        return map;
    }

    private static void checkValidEndpoint(String endpointName) {
        if (!vertices.containsKey(endpointName)) {
            throw new IllegalArgumentException(endpointName);
        }
    }

    //SHORTEST PATH
    // Djikstra's Algorithm to find shortest path.
    private static void findShortestPath(String startName, String endName) {

        //get the actual start and end verticies
        Vertex<String> start = vertices.get(startName);
        Vertex<String> end = vertices.get(endName);

        //if the locations are the same, there is no path
        if (startName.equals(endName)) {
            System.out.println("No path found");
            return;
        }

        double totalDist = 0;

        //Obtain arraylist of vertices
        ArrayList<Vertex<String>> vList =
            (ArrayList<Vertex<String>>)graph.vertices();

        //Instantiate priority queue with custom comparator
        PriorityQueue<Vertex<String>> pq =
            new PriorityQueue<Vertex<String>>(new Comparator<Vertex<String>>() {
                public int compare(Vertex<String> a, Vertex<String> b) {
                    double result = graph.totalCost(a) - graph.totalCost(b);
                    if (result < 0) {
                        //if (a - b) is less than 0
                        //a has a higher priority (it has a smaller distance)
                        return -1;
                    } else if (result > 0) {
                        //if (a - b) is less than 0
                        //a has a lower priority (it has a bigger distance)
                        return 1;
                    }
                    return 0; //equivelent
                }
            });

        //reset all verticies to have the max distance and to be unvisited
        for (Vertex<String> v : vList) {
            if (!v.equals(start)) {
                graph.totalCost(v, MAX_DISTANCE);
                graph.visited(v, false);
            }
        }

        //add the starting vertex to the priority queue
        pq.add(start);
        Vertex<String> newSmallest = null;

        //repeat until priority queue is empty
        while (!pq.isEmpty()) {

            newSmallest = pq.remove(); //dequeue

            ArrayList<Edge<String>> immediateRoads =
                new ArrayList<Edge<String>>();
            ArrayList<Vertex<String>> neighbours =
                new ArrayList<Vertex<String>>();

            //For every adjacent road from the newSmallest/current vertex
            for (Edge<String> road : graph.outgoing(newSmallest)) {
                //If the vertex of that road is unvisited
                Vertex<String> neighbour = graph.to(road);
                if (!graph.visited(neighbour)) {
                    //calculate the path
                    double altPath = graph.totalCost(newSmallest) +
                                     (double)graph.label(road);

                    if (altPath < graph.totalCost(neighbour)) {
                        graph.totalCost(neighbour, altPath);
                        //set the label of the neighboring vertex to the edge
                        graph.label(neighbour, road); //road is prev
                        graph.visited(newSmallest, true);

                        pq.add(neighbour);
                    }
                }
            }
        }

        //get the path of edges
        List<Edge<String>> path = getPath(end, start);

        //get the total distance traveled by adding up all the edge labels
        for (Edge<String> road : path) {
            totalDist += (double)graph.label(road);
        }

        //print the path
        printPath(path, totalDist);
    }

    private static List<Edge<String>> getPath(Vertex<String> end,
                                              Vertex<String> start) {

        if (graph.label(end) != null) {

            //create an arrayList of roads
            List<Edge<String>> path = new ArrayList<>();

            //current vertex is intialized to end vertex
            Vertex<String> cur = end;
            Edge<String> road;

            while (cur != start) {
                //get the next edge in the path (stored in the vertex's label)
                //this assumes that there will always be a label that is not null....
                road = (Edge<String>)graph.label(cur);
                //add the edge to the path
                path.add(road);
                //get the next vertex along the path (the edge's "from" vertex)
                cur = graph.from(road);
            }
            return path;
        }
        return null;
    }

    private static void printPath(List<Edge<String>> path,
                                  double totalDistance) {

        //if the end vertex didn't have it's label set to an edge then a path was never discovered
        if (path == null) {
            System.out.println("No path found");
            return;
        }

        System.out.println("Total Distance: " + totalDistance);
        //loop traverses the arrayList from back to front...
        //...since the starting edge is at the back
        for (int i = path.size() - 1; i >= 0; i--) {
            //print the data in the edge (name) and the label (distance)
            System.out.println(path.get(i).get() + " " +
                               graph.label(path.get(i)));
        }
    }
}
