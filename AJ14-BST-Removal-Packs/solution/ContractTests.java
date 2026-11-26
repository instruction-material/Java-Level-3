import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var tree = new Main.Tree();
        for (int key : new int[]{12, 3, 20, 7, 16, 4, 23}) tree.insert(key);
        expect(true, tree.remove(12));
        expect(List.of(3, 4, 7, 16, 20, 23), tree.keys());
        expect(true, tree.remove(3));
        expect(true, tree.remove(23));
        expect(false, tree.remove(23));
        expect(List.of(4, 7, 16, 20), tree.keys());
        expect(4, tree.size());
        tree.clear();
        tree.clear();
        expect(List.of(), tree.keys());
        expect(0, tree.size());
        var expected = new java.util.TreeSet<Integer>();
        var random = new java.util.SplittableRandom(14);
        for (int step = 0; step < 2048; step++) {
            int key = random.nextInt(-120, 121);
            if (random.nextBoolean()) {
                expected.add(key);
                tree.insert(key);
            } else {
                expect(expected.remove(key), tree.remove(key));
            }
            expect(expected.size(), tree.size());
            expect(List.copyOf(expected), tree.keys());
        }

        System.out.println("Binary search tree checks passed");
    }
}
