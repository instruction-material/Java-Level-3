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
        } catch (IllegalArgumentException correct) {
            return;
        }
        throw new AssertionError("Expected an invalid input to be rejected");
    }

    public static void main(String[] args) {
        var low = new Main.Node(Integer.MIN_VALUE, null, null);
        var high = new Main.Node(Integer.MAX_VALUE, null, null);
        expect(true, Main.isSearchTree(new Main.Node(0, low, high)));
        expect(true, Main.isSearchTree(null));
        expect(false, Main.isSearchTree(new Main.Node(12,
                new Main.Node(3, null, new Main.Node(14, null, null)), null)));
        expect(false, Main.isSearchTree(new Main.Node(7, new Main.Node(7, null, null), null)));

        var deleted = new Main.Slot(Main.State.DELETED, 5);
        var matching = new Main.Slot(Main.State.LIVE, 2);
        var empty = new Main.Slot(Main.State.EMPTY, 0);
        var live = new Main.Slot(Main.State.LIVE, 0);
        var wrap = List.of(matching, empty, deleted);
        expect(new Main.Probe(0, true), Main.probe(wrap, 2, false));
        expect(new Main.Probe(2, false), Main.probe(wrap, 5, true));
        expect(new Main.Probe(-1, false), Main.probe(List.of(deleted, deleted), 12, false));
        expect(new Main.Probe(0, false), Main.probe(List.of(deleted, deleted), 12, true));
        expect(new Main.Probe(-1, false), Main.probe(List.of(live), -1, true));
        expect(new Main.Probe(0, false), Main.probe(List.of(empty), Integer.MIN_VALUE, true));
        rejects(() -> Main.probe(List.of(), 0, true));

        int big = Integer.MAX_VALUE;
        int[][] matrix = {{-1, big, 0}, {-1, -1, big}, {-1, -1, -1}};
        expect(java.util.OptionalLong.of(4294967294L), Main.pathCost(matrix, List.of(0, 1, 2)));
        expect(java.util.OptionalLong.of(0), Main.pathCost(matrix, List.of(0, 2)));
        expect(java.util.OptionalLong.of(0), Main.pathCost(matrix, List.of(1)));
        expect(java.util.OptionalLong.empty(), Main.pathCost(matrix, List.of(2, 0)));
        expect(java.util.OptionalLong.empty(), Main.pathCost(matrix, List.of()));
        rejects(() -> Main.pathCost(matrix, List.of(2, 0, 4)));
        rejects(() -> Main.pathCost(new int[][]{{-2}}, List.of(0)));
        if (args.length == 1 && args[0].equals("hops")) {
            expect(java.util.OptionalInt.of(1), Main.fewestHops(matrix, 0, 2));
            expect(java.util.OptionalInt.of(0), Main.fewestHops(matrix, 0, 0));
            expect(java.util.OptionalInt.empty(), Main.fewestHops(matrix, 2, 0));
            expect(java.util.OptionalInt.of(2), Main.fewestHops(new int[][]{{-1, 0, -1}, {-1, -1, 4}, {0, -1, -1}}, 0, 2));
            rejects(() -> Main.fewestHops(matrix, 0, 4));
            System.out.println("Hop checks passed");
            return;
        }

        System.out.println("Advanced structures checks passed");
    }
}
