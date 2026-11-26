import java.util.*;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void invalid(Runnable work) {
        try {
            work.run();
        } catch (IllegalArgumentException | ArithmeticException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input");
    }

    public static void main(String[] args) {
        same(OptionalInt.of(7), Main.count(new Scanner("bad\n-3\n 7")));
        same(OptionalInt.of(0), Main.count(new Scanner("2147483648\n0")));
        same(OptionalInt.empty(), Main.count(new Scanner("")));
        same(OptionalInt.empty(), Main.count(new Scanner("3.5\n-2")));
        System.out.println("Review checks passed");
    }
}
