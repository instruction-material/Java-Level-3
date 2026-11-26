import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class Main {
    record Node(int value, Node left, Node right) {}
    enum State { EMPTY, LIVE, DELETED }
    record Slot(State state, int key) {}
    record Probe(int index, boolean found) {}

    static boolean isSearchTree(Node root) {
        return ordered(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    static boolean ordered(Node root, long low, long high) {
        // TODO: Check every tree key against its inherited bounds.
        throw new UnsupportedOperationException("Check every tree key against its inherited bounds");
    }

    static Probe probe(List<Slot> table, int key, boolean inserting) {
        // TODO: Find an equal live key or select a reusable bucket.
        throw new UnsupportedOperationException("Find an equal live key or select a reusable bucket");
    }

    static OptionalLong pathCost(int[][] graph, List<Integer> path) {
        // TODO: Validate each directed edge and total its weight.
        throw new UnsupportedOperationException("Validate each directed edge and total its weight");
    }

    static OptionalInt fewestHops(int[][] graph, int start, int target) {
        // TODO: Find the minimum number of edges with breadth-first search.
        throw new UnsupportedOperationException("Find the minimum number of edges with breadth-first search");
    }

    static void checkMatrix(int[][] graph) {
        for (int[] row : graph) {
            if (row == null || row.length != graph.length) {
                throw new IllegalArgumentException("Expected a square matrix");
            }
            for (int weight : row) {
                if (weight < -1) throw new IllegalArgumentException("Only minus one represents a missing edge");
            }
        }
    }

    static int checkedVertex(int[][] graph, int vertex) {
        if (vertex < 0 || vertex >= graph.length) {
            throw new IllegalArgumentException("Unknown vertex " + vertex);
        }
        return vertex;
    }

    public static void main(String[] args) {
        int[][] graph = {{-1, 2, 8}, {-1, -1, 3}, {-1, -1, -1}};
        if (args.length == 1 && args[0].equals("hops")) {
            System.out.println(fewestHops(graph, 0, 2).orElseThrow());
            return;
        }
        var tree = new Node(12, new Node(3, null, new Node(7, null, null)), new Node(20, null, null));
        var slots = List.of(new Slot(State.EMPTY, 0), new Slot(State.LIVE, 6),
                new Slot(State.DELETED, 11), new Slot(State.LIVE, 1), new Slot(State.EMPTY, 0));
        System.out.println(isSearchTree(tree));
        System.out.println(probe(slots, 1, false));
        System.out.println(probe(slots, 16, true));
        System.out.println(pathCost(graph, List.of(0, 1, 2)).orElseThrow());
        System.out.println(pathCost(graph, List.of(2, 0)));
    }
}
