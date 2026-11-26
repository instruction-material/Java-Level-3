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
        same(150L, new Main.Standard(2).feeCents());
        same(200L, new Main.Express(2).feeCents());
        same(100L, new Main.Express(0).feeCents());
        same(107374182450L, new Main.Express(Integer.MAX_VALUE).feeCents());
        invalid(() -> new Main.Standard(-1));
        System.out.println("Review checks passed");
    }
}
