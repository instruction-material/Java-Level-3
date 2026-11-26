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
        var shelf = new Main.Shelf(2);
        same(true, shelf.checkout());
        same(1, shelf.copies());
        same(true, shelf.checkout());
        same(false, shelf.checkout());
        same(0, shelf.copies());
        invalid(() -> new Main.Shelf(-1));
        System.out.println("Review checks passed");
    }
}
