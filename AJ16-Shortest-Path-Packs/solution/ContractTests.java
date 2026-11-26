import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected rejection");
    }

    public static void main(String[] args) {
        int upper = Integer.MAX_VALUE;
        int[][] wide = {{-1, upper, -1, -1}, {-1, -1, upper, -1}, {-1, -1, -1, -1}, {-1, -1, -1, -1}};
        expect(new Main.Route(4294967294L, List.of(0, 1, 2)), Main.shortest(wide, 0, 2).orElseThrow());
        expect(java.util.Optional.empty(), Main.shortest(wide, 0, 3));
        expect(new Main.Route(0, List.of(0)), Main.shortest(wide, 0, 0).orElseThrow());
        int[][] zero = {{-1, 0, 2}, {0, -1, 0}, {-1, -1, -1}};
        expect(new Main.Route(0, List.of(0, 1, 2)), Main.shortest(zero, 0, 2).orElseThrow());
        rejects(() -> Main.shortest(new int[][]{{-2}}, 0, 0));
        rejects(() -> Main.shortest(new int[][]{{0, 1}}, 0, 0));
        rejects(() -> Main.shortest(wide, 0, 4));

        var random = new java.util.SplittableRandom(16);
        for (int sample = 0; sample < 12; sample++) {
            int n = 7;
            int[][] matrix = new int[n][n];
            long[][] answer = new long[n][n];
            for (int from = 0; from < n; from++) {
                for (int to = 0; to < n; to++) {
                    matrix[from][to] = random.nextInt(3) == 0 ? random.nextInt(10) : -1;
                    answer[from][to] = from == to ? 0 : matrix[from][to] == -1 ? 1_000_000L : matrix[from][to];
                }
            }
            for (int middle = 0; middle < n; middle++) {
                for (int from = 0; from < n; from++) {
                    for (int to = 0; to < n; to++) {
                        answer[from][to] = Math.min(answer[from][to], answer[from][middle] + answer[middle][to]);
                    }
                }
            }
            for (int from = 0; from < n; from++) {
                for (int to = 0; to < n; to++) {
                    var actual = Main.shortest(matrix, from, to);
                    if (answer[from][to] == 1_000_000L) {
                        expect(java.util.Optional.empty(), actual);
                        continue;
                    }
                    var route = actual.orElseThrow();
                    expect(answer[from][to], route.cost());
                    expect(from, route.vertices().getFirst());
                    expect(to, route.vertices().getLast());
                    long cost = 0;
                    for (int step = 1; step < route.vertices().size(); step++) {
                        int weight = matrix[route.vertices().get(step-1)][route.vertices().get(step)];
                        if (weight < 0) throw new AssertionError("Path contains a missing edge");
                        cost += weight;
                    }
                    expect(route.cost(), cost);
                }
            }
        }

        System.out.println("Graph checks passed");
    }
}
