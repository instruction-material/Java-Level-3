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
        var a = new Main.Entry(Integer.MIN_VALUE, -2);
        var b = new Main.Entry(4, 6);
        var before = List.of(a, b);
        expect(true, Main.valid(before, List.of(List.of(), List.of(a, b), List.of())));
        expect(false, Main.valid(before, List.of(List.of(), List.of(a, a, b), List.of())));
        expect(false, Main.valid(before, List.of(List.of(), List.of(b), List.of(a))));
        expect(false, Main.valid(before, List.of(List.of(), List.of(a, new Main.Entry(4, 2)), List.of())));
        expect(false, Main.valid(before, List.of(List.of(), List.of(a), List.of())));
        expect(false, Main.valid(List.of(a, a), List.of(List.of(), List.of(a), List.of())));
        expect(false, Main.valid(List.of(), List.of()));
        expect(true, Main.valid(List.of(), List.of(List.of(), List.of())));

        System.out.println("Advanced structures checks passed");
    }
}
