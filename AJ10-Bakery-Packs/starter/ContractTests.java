import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;

public class ContractTests {

    static void expect(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void expectArray(int[] expected, int[] actual) {
        expect(Arrays.toString(expected), Arrays.toString(actual));
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("batches")) {
            var item = new Bakery.Product("Roll", "Rosemary", List.of("flour"), 6);
            expect(0, item.batchesFor(0));
            expect(1, item.batchesFor(6));
            expect(2, item.batchesFor(10));
            expect(357913942, item.batchesFor(Integer.MAX_VALUE));
            try {
                item.batchesFor(-1);
                throw new AssertionError("Negative order");
            } catch (IllegalArgumentException expected) {
                // Negative orders are outside the contract.
            }
            System.out.println("Batch checks passed");
            return;
        }
        try (var input = new java.util.Scanner("Bay\n7 Pier\nJo\nBaker\n1700\nRoll\nPlain\nflour, water\n8\n")) {
            Bakery bakery = Main.readBakery(input);
            expect("7 Pier", bakery.address());
            expect("Jo | Baker | Bay", bakery.workers().getFirst().badge());
            expect(1700, bakery.workers().getFirst().hourlyCents());
            expect(List.of("flour", "water"), bakery.products().getFirst().ingredients());
            expect(8, bakery.products().getFirst().batchSize());
        }
        var mutable = new java.util.ArrayList<>(List.of("flour"));
        var item = new Bakery.Product("Roll", "Plain", mutable, 6);
        mutable.add("salt");
        expect(List.of("flour"), item.ingredients());
        try {
            new Bakery.Product("Roll", "Plain", List.of("flour"), 0);
            throw new AssertionError("Zero batch");
        } catch (IllegalArgumentException expected) {
            // A product always has a positive batch size.
        }
        System.out.println("Sustainable-programming checks passed");
    }
}
