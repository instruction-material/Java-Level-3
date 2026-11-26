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
        same(OptionalDouble.of(89.5), Main.average(new int[]{89, 90}));
        same("B", Main.letter(Main.average(new int[]{89, 90}).orElseThrow()));
        same("A", Main.letter(90));
        same(OptionalDouble.empty(), Main.average(new int[0]));
        invalid(() -> Main.average(new int[]{101}));
        System.out.println("Review checks passed");
    }
}
