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
        var first = new Main.Student(" Cedar ", 7);
        var second = new Main.Student("Rowan", 9);
        first.markPresent(true);
        same("Cedar #7 present", first.status());
        same("Rowan #9 absent", second.status());
        invalid(() -> new Main.Student(" ", 5));
        invalid(() -> new Main.Student("Pine", 0));
        if (args.length > 0) same(1, Main.Student.countPresent(List.of(first, second)));
        System.out.println("Review checks passed");
    }
}
