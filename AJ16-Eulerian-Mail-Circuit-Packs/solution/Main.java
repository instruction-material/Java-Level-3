import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Street(int first, int second) {}
    enum RouteKind { CIRCUIT, TRAIL, NEITHER }

    static RouteKind classify(int vertices, List<Street> streets) {
        if (vertices < 0) throw new IllegalArgumentException("Invalid vertex count");
        var neighbors = new ArrayList<List<Integer>>(vertices);
        for (int i = 0; i < vertices; i++) neighbors.add(new ArrayList<>());
        for (Street street : streets) {
            int first = street.first();
            int second = street.second();
            if (first < 0 || second < 0 || first >= vertices || second >= vertices) {
                throw new IllegalArgumentException("Unknown street endpoint");
            }
            neighbors.get(first).add(second);
            neighbors.get(second).add(first);
        }
        if (vertices == 0) return RouteKind.NEITHER;
        if (streets.isEmpty()) return RouteKind.CIRCUIT;

        var queue = new ArrayDeque<Integer>();
        boolean[] seen = new boolean[vertices];
        int start = streets.getFirst().first();
        queue.addLast(start);
        seen[start] = true;
        while (!queue.isEmpty()) {
            for (int neighbor : neighbors.get(queue.removeFirst())) {
                if (!seen[neighbor]) {
                    seen[neighbor] = true;
                    queue.addLast(neighbor);
                }
            }
        }

        int odd = 0;
        for (int i = 0; i < vertices; i++) {
            int degree = neighbors.get(i).size();
            if (degree != 0 && !seen[i]) return RouteKind.NEITHER;
            odd += degree % 2;
        }
        if (odd == 0) return RouteKind.CIRCUIT;
        return odd == 2 ? RouteKind.TRAIL : RouteKind.NEITHER;
    }

    public static void main(String[] args) {
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(1, 2), new Street(2, 0))));
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(1, 2))));
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(2, 3))));
    }
}
