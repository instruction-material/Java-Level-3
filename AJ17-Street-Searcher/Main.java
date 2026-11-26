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
        while (locations.isEmpty()) {
            if (!loadData()) return;
        }

        //print out available locations
        System.out.println("\nLOCATIONS:");
        for (String locationName : locations.keySet()) {
            System.out.println(locationName);
        }
        System.out.println();

        pathFinder = new Pathfinder(graph, locations);
        while (runProgram()) {
            // The next iteration accepts another route query.
        }
    }

    private static boolean runProgram() {
        //this is the main functionality of the program
        try {
            System.out.print("Enter a starting location: ");
            if (!scanner.hasNextLine()) return false;
            String startName = scanner.nextLine().strip();
            if (!graph.hasVertex(startName)) {
                throw new IllegalArgumentException();
            }
            System.out.print("Enter an ending location: ");
            if (!scanner.hasNextLine()) return false;
            String endName = scanner.nextLine().strip();
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
        return true;
    }

    private static boolean loadData() {
        System.out.print("Please enter a file name: ");
        if (!scanner.hasNextLine()) return false;
        File file = new File(scanner.nextLine());
        int lineNumber = 0;

        try (Scanner lines = new Scanner(file)) {
            while (lines.hasNextLine()) {
                String line = lines.nextLine().strip();
                lineNumber++;
                if (!line.isEmpty()) parseTokens(line.split("\\s+"));
            }
            if (lines.ioException() != null) {
                throw lines.ioException();
            }
            if (locations.isEmpty()) throw new IllegalArgumentException("File has no roads");

            System.out.println("\nNetwork Loaded!");
            System.out.println("Loaded " + graph.getEdgesCount() + " roads.");
            System.out.println("Loaded " + locations.size() + " locations.");
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            clearNetwork();
        } catch (IllegalArgumentException ex) {
            System.out.printf("ERROR at road file line %d: %s%n", lineNumber, ex.getMessage());
            clearNetwork();
        }
        return true;
    }

    private static void clearNetwork() {
        graph = new WeightedGraph<>();
        locations.clear();
    }

    private static void parseTokens(String[] tokens) {
        if (tokens.length != 4) throw new IllegalArgumentException("Expected four road fields");

        // Fields are source coordinates, destination coordinates, cost and source--destination.
        String[] names = tokens[3].split("--", -1);
        if (names.length != 2 || names[0].isBlank() || names[1].isBlank()) {
            throw new IllegalArgumentException("Expected source--destination names");
        }
        Location from = parseLocation(names[0], tokens[0]);
        Location to = parseLocation(names[1], tokens[1]);
        double distance = Double.parseDouble(tokens[2]);
        if (!Double.isFinite(distance) || distance < 0) {
            throw new IllegalArgumentException("Road cost must be finite and nonnegative");
        }

        locations.putIfAbsent(from.getName(), from);
        locations.putIfAbsent(to.getName(), to);
        graph.addEdge(from.getName(), to.getName(), distance);
    }

    private static Location parseLocation(String name, String coordinateText) {
        String[] coordinates = coordinateText.split(",", -1);
        if (coordinates.length != 2) throw new IllegalArgumentException("Expected two coordinates");
        double latitude = Double.parseDouble(coordinates[0]);
        double longitude = Double.parseDouble(coordinates[1]);
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)) {
            throw new IllegalArgumentException("Coordinates must be finite");
        }
        return new Location(name, latitude, longitude);
    }
}
