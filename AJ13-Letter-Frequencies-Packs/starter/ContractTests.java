import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

public class ContractTests {
    private static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var counts = Main.frequencies("Baa BB! 2, café É.");
        expect(java.util.Map.of(97, 3, 98, 3, 99, 1, 102, 1, 233, 2), counts);
        expect(List.of(97, 98, 99, 102, 233), List.copyOf(counts.keySet()));
        expect(OptionalInt.of('a'), Main.winner(counts));
        var descending = new java.util.TreeMap<Integer, Integer>(java.util.Comparator.reverseOrder());
        descending.putAll(counts);
        expect(OptionalInt.of('a'), Main.winner(descending));
        expect(OptionalInt.empty(), Main.winner(Main.frequencies("   2, ?!")));
        expect(OptionalInt.empty(), Main.winner(Main.frequencies("")));
        var supplementary = Main.frequencies("𐐀 𐐨 ¿x?");
        expect(2, supplementary.get(0x10428));
        expect(OptionalInt.of(0x10428), Main.winner(supplementary));

        System.out.println("Priority queue and map checks passed");
    }
}
