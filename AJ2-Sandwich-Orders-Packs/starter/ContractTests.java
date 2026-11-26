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
        same("lc", Main.order(List.of("lettuce", "onion", "cheese"), List.of("yes", "NO", " yes ")));
        same("", Main.order(List.of(), List.of()));
        invalid(() -> Main.order(List.of("lettuce"), List.of()));
        invalid(() -> Main.order(List.of("lettuce"), List.of("maybe")));
        System.out.println("Review checks passed");
    }
}
