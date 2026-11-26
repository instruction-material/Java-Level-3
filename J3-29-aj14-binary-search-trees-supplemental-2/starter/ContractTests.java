import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var root = Main.add(null, 12);
        for (int key : new int[]{3, 20, 7, -4, 18, 23}) Main.add(root, key);
        expect(List.of(7, 12, 18, 20), Main.within(root, 7, 20));
        expect(List.of(12), Main.within(root, 12, 12));
        expect(List.of(), Main.within(root, 10, 9));
        expect(List.of(), Main.within(null, 0, 10));
        expect(List.of(-4, 3, 7, 12, 18, 20, 23), Main.within(root, Integer.MIN_VALUE, Integer.MAX_VALUE));

        System.out.println("Binary search tree checks passed");
    }
}
