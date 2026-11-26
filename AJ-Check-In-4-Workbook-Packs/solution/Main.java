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
        if (root == null) return true;
        return low < root.value() && root.value() < high
                && ordered(root.left(), low, root.value())
                && ordered(root.right(), root.value(), high);
    }

    static Probe probe(List<Slot> table, int key, boolean inserting) {
        if (table.isEmpty()) throw new IllegalArgumentException("Empty table");
        int index = Math.floorMod(key, table.size());
        int firstDeleted = -1;
        for (int scanned = 0; scanned < table.size(); scanned++) {
            Slot slot = table.get(index);
            if (slot.state() == State.EMPTY) {
                if (!inserting) return new Probe(-1, false);
                return new Probe(firstDeleted < 0 ? index : firstDeleted, false);
            }
            if (slot.state() == State.LIVE && slot.key() == key) return new Probe(index, true);
            if (inserting && slot.state() == State.DELETED && firstDeleted < 0) firstDeleted = index;
            index = (index + 1) % table.size();
        }
        return new Probe(firstDeleted, false);
    }

    static OptionalLong pathCost(int[][] graph, List<Integer> path) {
        checkMatrix(graph);
        if (path.isEmpty()) return OptionalLong.empty();
        int previous = checkedVertex(graph, path.getFirst());
        long cost = 0;
        boolean exists = true;
        for (int i = 1; i < path.size(); i++) {
            int next = checkedVertex(graph, path.get(i));
            int edge = graph[previous][next];
            if (edge < 0) exists = false;
            else cost += edge;
            previous = next;
        }
        return exists ? OptionalLong.of(cost) : OptionalLong.empty();
    }

    static OptionalInt fewestHops(int[][] graph, int start, int target) {
        checkMatrix(graph);
        checkedVertex(graph, start);
        checkedVertex(graph, target);
        int[] hops = new int[graph.length];
        Arrays.fill(hops, -1);
        var queue = new ArrayDeque<Integer>();
        queue.addLast(start);
        hops[start] = 0;
        while (!queue.isEmpty()) {
            int vertex = queue.removeFirst();
            if (vertex == target) return OptionalInt.of(hops[vertex]);
            for (int next = 0; next < graph.length; next++) {
                if (graph[vertex][next] >= 0 && hops[next] < 0) {
                    hops[next] = hops[vertex] + 1;
                    queue.addLast(next);
                }
            }
        }
        return OptionalInt.empty();
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
