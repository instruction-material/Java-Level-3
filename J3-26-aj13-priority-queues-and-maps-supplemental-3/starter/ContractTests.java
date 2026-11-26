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
        var catalog = Main.catalog(List.of(new Main.Update(" I ", 5), new Main.Update("i", 0),
                new Main.Update("Bulb", 7)));
        expect(java.util.Map.of("i", 0, "bulb", 7), catalog);
        expect(OptionalInt.of(0), Main.lookup(catalog, " I "));
        expect(OptionalInt.of(7), Main.lookup(catalog, "bulb"));
        expect(OptionalInt.empty(), Main.lookup(catalog, "Cable"));
        expect(OptionalInt.empty(), Main.lookup(Main.catalog(List.of()), "I"));
        for (var invalid : List.of(new Main.Update(" ", 0), new Main.Update("bulb", -1))) {
            try {
                Main.catalog(List.of(invalid));
                throw new AssertionError("Accepted invalid update");
            } catch (IllegalArgumentException expected) {
                // Invalid updates do not produce a catalog.
            }
        }

        System.out.println("Priority queue and map checks passed");
    }
}
