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
        var values = new ArrayList<>(List.of(5, 2, -4, 2, 7, 0));
        same(List.of(2, -4, 2, 0), Main.evens(values));
        same(List.of(5, 2, -4, 2, 7, 0), values);
        same(List.of(), Main.evens(List.of()));
        same(List.of(), Main.evens(List.of(-1, 5)));
        same(false, Main.hasZeroPair(List.of(Integer.MIN_VALUE, Integer.MIN_VALUE)));
        same(false, Main.hasZeroPair(List.of(0)));
        same(true, Main.hasZeroPair(List.of(0, 0)));
        same(true, Main.hasZeroPair(List.of(2, -2)));
        var mutable = new ArrayList<>(List.of(2, -4, 3, -4));
        Main.removeFirstMinimum(mutable);
        same(List.of(2, 3, -4), mutable);
        Main.appendTotal(mutable);
        same(List.of(2, 3, -4, 1), mutable);
        var empty = new ArrayList<Integer>();
        Main.removeFirstMinimum(empty);
        Main.appendTotal(empty);
        same(List.of(0), empty);
        invalid(() -> Main.appendTotal(new ArrayList<>(List.of(Integer.MAX_VALUE, 1))));
        System.out.println("Review checks passed");
    }
}
