import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ShortestPathTree {
    private record Candidate(int vertex, long distance) {}

    int[][] graph;
    ArrayList<Vertex<Integer>> vertices;
    private PriorityQueue<Candidate> unvisited;

    public void dijkstra(int[][] graph, int source) {
        validate(graph, source);
        this.graph = graph;
        vertices = new ArrayList<>(graph.length);
        unvisited = new PriorityQueue<>(
                Comparator.comparingLong(Candidate::distance).thenComparingInt(Candidate::vertex));

        for (int i = 0; i < graph.length; i++) {
            vertices.add(new Vertex<>(i));
        }
        vertices.get(source).setDistance(0);
        unvisited.add(new Candidate(source, 0));

        while (!unvisited.isEmpty()) {
            Candidate candidate = unvisited.remove();
            Vertex<Integer> current = vertices.get(candidate.vertex());
            if (candidate.distance() == current.getDistance()) {
                updateNeighborDistances(current);
            }
        }
        printSolution();
    }

    private static void validate(int[][] graph, int source) {
        if (graph == null || source < 0 || source >= graph.length) {
            throw new IllegalArgumentException("Source must be a vertex in the graph");
        }
        for (int[] row : graph) {
            if (row == null || row.length != graph.length) {
                throw new IllegalArgumentException("The adjacency matrix must be square");
            }
            for (int weight : row) {
                if (weight < 0) throw new IllegalArgumentException("Edge weights cannot be negative");
            }
        }
    }

    public void updateNeighborDistances(Vertex<Integer> current) {
        int[] weights = graph[current.getLabel()];
        if (current.getDistance() == Long.MAX_VALUE) return;

        for (int index = 0; index < weights.length; index++) {
            int weight = weights[index];
            // The original matrix uses zero to indicate no edge.
            if (weight == 0) continue;

            Vertex<Integer> neighbor = vertices.get(index);
            long alternate = current.getDistance() + weight;
            if (alternate < neighbor.getDistance()) {
                neighbor.setDistance(alternate);
                neighbor.setPrevious(current);
                unvisited.add(new Candidate(index, alternate));
            }
        }
    }

    public void printSolution() {
        for (Vertex<Integer> vertex : vertices) {
            System.out.println(vertex);
        }
    }
}
