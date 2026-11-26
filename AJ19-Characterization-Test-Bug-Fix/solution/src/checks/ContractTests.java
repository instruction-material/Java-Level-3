package checks;

import java.util.Objects;
import quotes.Shipping;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input");
    }

    public static void main(String[] args) {
        var shipping = new Shipping();
        same(250, shipping.charge(0));
        same(250, shipping.charge(4_999));
        same(0, shipping.charge(5_001));
        rejects(() -> shipping.charge(-1));
        if (args.length == 1 && args[0].equals("characterize")) {
            same(250, shipping.charge(5_000));
            System.out.println("Original threshold behavior captured");
            return;
        }
        same(0, shipping.charge(5_000));
        System.out.println("AJ19 checks passed");
    }
}
