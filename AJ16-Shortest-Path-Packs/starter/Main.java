import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class Main {
    record Candidate(int vertex, long cost) {}
    record Route(long cost, List<Integer> vertices) {}

    static Optional<Route> shortest(int[][] graph, int source, int target) {
        // TODO: Relax weighted edges and reconstruct the shortest route.
        throw new UnsupportedOperationException("Relax weighted edges and reconstruct the shortest route");
    }

    static void validate(int[][] graph, int source, int target) {
        if (graph == null || source < 0 || target < 0 || source >= graph.length || target >= graph.length) {
            throw new IllegalArgumentException("Endpoints must be vertices in the graph");
        }
        for (int[] row : graph) {
            if (row == null || row.length != graph.length) {
                throw new IllegalArgumentException("Expected a square matrix");
            }
            for (int weight : row) {
                if (weight < -1) throw new IllegalArgumentException("Invalid negative edge");
            }
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
            {-1, 7, 2, -1, -1},
            {-1, -1, -1, 2, -1},
            {-1, 1, -1, 8, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1}
        };
        var route = shortest(graph, 0, 3).orElseThrow();
        System.out.println(route.cost());
        System.out.println(route.vertices());
        System.out.println(shortest(graph, 0, 4).isEmpty());
    }
}
