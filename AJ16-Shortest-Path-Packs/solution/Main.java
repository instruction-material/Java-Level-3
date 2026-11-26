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
        validate(graph, source, target);
        long[] costs = new long[graph.length];
        int[] previous = new int[graph.length];
        Arrays.fill(costs, Long.MAX_VALUE);
        Arrays.fill(previous, -1);
        costs[source] = 0;
        var frontier = new PriorityQueue<>(
                Comparator.comparingLong(Candidate::cost).thenComparingInt(Candidate::vertex));
        frontier.add(new Candidate(source, 0));
        while (!frontier.isEmpty()) {
            var current = frontier.remove();
            int from = current.vertex();
            if (current.cost() != costs[from]) continue;
            if (from == target) {
                var path = new ArrayDeque<Integer>();
                for (int vertex = target; vertex != -1; vertex = previous[vertex]) path.addFirst(vertex);
                return Optional.of(new Route(costs[target], List.copyOf(path)));
            }
            for (int to = 0; to < graph.length; to++) {
                int weight = graph[from][to];
                if (weight == -1) continue;
                long candidate = current.cost() + weight;
                if (candidate < costs[to]) {
                    costs[to] = candidate;
                    previous[to] = from;
                    frontier.add(new Candidate(to, candidate));
                }
            }
        }
        return Optional.empty();
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
