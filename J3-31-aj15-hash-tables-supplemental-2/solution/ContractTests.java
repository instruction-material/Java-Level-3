import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var buckets = Main.table(3);
        Main.put(buckets, -1, 0);
        Main.put(buckets, 2, 7);
        Main.put(buckets, -1, -3);
        expect(2, buckets.get(2).size());
        expect(java.util.OptionalInt.of(-3), Main.get(buckets, -1));
        expect(java.util.OptionalInt.of(7), Main.get(buckets, 2));
        expect(java.util.OptionalInt.empty(), Main.get(buckets, 5));
        Main.put(buckets, Integer.MIN_VALUE, 0);
        expect(java.util.OptionalInt.of(0), Main.get(buckets, Integer.MIN_VALUE));

        System.out.println("Hash table checks passed");
    }
}
