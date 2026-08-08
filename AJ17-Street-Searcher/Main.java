import java.io.*;
import java.util.*;

class Main {

    public static WeightedGraph<String, Double> graph;
    public static HashMap<String, Location> locations;
    public static Pathfinder pathFinder;
    public static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        graph = new WeightedGraph<>();
        locations = new HashMap<>();

        //load data from a file
        System.out.println("\nWelcome to Street Searcher!");
        while (locations.size() <= 0) {
            loadData();
        }

        //print out available locations
        System.out.println("\nLOCATIONS:");
        for (String locationName : locations.keySet()) {
            System.out.println(locationName);
        }
        System.out.println();

        //run the main loop forever
        pathFinder = new Pathfinder(graph, locations);
        while (true) {
            runProgram();
        }
    }

    private static void runProgram() {
        //this is the main functionality of the program
        try {
            System.out.print("Enter a starting location: ");
            String startName = scanner.nextLine();
            if (!graph.hasVertex(startName)) {
                throw new IllegalArgumentException();
            }
            System.out.print("Enter an ending location: ");
            String endName = scanner.nextLine();
            if (!graph.hasVertex(endName)) {
                throw new IllegalArgumentException();
            }

            //use the pathfinder to find the shortest path
            Location start = locations.get(startName);
            Location end = locations.get(endName);
            pathFinder.findShortestPath(start, end);

        } catch (IllegalArgumentException ex) {
            System.out.println("That location doesn't exist! Try again!");
        }
    }

    private static void loadData() {

        try {
            //reset variables
            graph = new WeightedGraph<>();
            locations = new HashMap<>();

            //get filename
            System.out.print("Please enter a file name: ");
            File file = new File(scanner.nextLine());
            Scanner fileScanner = new Scanner(file);

            //parse every line in the file
            while (fileScanner.hasNext()) {
                String[] tokens = fileScanner.nextLine().split(" ");
                parseTokens(tokens);
            }
            fileScanner.close();

            //print success!
            System.out.println("\nNetwork Loaded!");
            System.out.println("Loaded " + graph.getEdgesCount() + " roads.");
            System.out.println("Loaded " + locations.size() + " locations.");

        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    private static void parseTokens(String[] tokens) {

        // every line contains 4 tokens:
        // 0: fromLat,fromLong
        // 1: toLat,toLong
        // 2: distance
        // 3: fromName--toName

        String[] names = tokens[3].split("--");

        // from location
        String fromName = names[0];
        String[] fromCoords = tokens[0].split(",");
        double fromLat = Double.parseDouble(fromCoords[0]);
        double fromLong = Double.parseDouble(fromCoords[1]);
        Location from = new Location(fromName, fromLat, fromLong);
        locations.putIfAbsent(fromName, from);

        // to location
        String toName = names[1];
        String[] toCoords = tokens[1].split(",");
        double toLat = Double.parseDouble(toCoords[0]);
        double toLong = Double.parseDouble(toCoords[1]);
        Location to = new Location(toName, toLat, toLong);
        locations.putIfAbsent(toName, to);

        // add to graph: verticies are names, weight is distance
        double distance = Double.parseDouble(tokens[2]);
        graph.addEdge(fromName, toName, distance);
    }
}
