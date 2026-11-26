import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var old = List.of(List.of(new Main.Entry(Integer.MIN_VALUE, "min"),
                new Main.Entry(5, "five"), new Main.Entry(-1, "negative")));
        var resized = Main.resize(old, 3);
        expect(List.of(new Main.Entry(Integer.MIN_VALUE, "min")), resized.get(1));
        expect(List.of(new Main.Entry(5, "five"), new Main.Entry(-1, "negative")), resized.get(2));
        expect(3, resized.stream().mapToInt(List::size).sum());
        expect(3, old.getFirst().size());
        expect(List.of(new Main.Entry(5, "five")), Main.resize(resized, 5).getFirst());
        try {
            Main.resize(old, 0);
            throw new AssertionError("Accepted zero capacity");
        } catch (IllegalArgumentException expected) {
            // A hash table always has at least one bucket.
        }

        System.out.println("Hash table checks passed");
    }
}
