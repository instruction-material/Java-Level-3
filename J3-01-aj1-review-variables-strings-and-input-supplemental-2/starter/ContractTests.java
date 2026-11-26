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
        same(14L, Main.fare(4));
        same(2L, Main.fare(0));
        same(6442450943L, Main.fare(Integer.MAX_VALUE));
        invalid(() -> Main.fare(-1));
        System.out.println("Review checks passed");
    }
}
