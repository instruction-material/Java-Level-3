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
        same("Hello, Mira! Try sketching a tree.", Main.reply(" Mira ", " DRAWING "));
        same("Hello, Sam! Try a short story.", Main.reply("Sam", "reading"));
        same("Hello, Sam! Tell me more about that activity.", Main.reply("Sam", "running"));
        System.out.println("Review checks passed");
    }
}
