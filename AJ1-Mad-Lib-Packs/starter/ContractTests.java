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
        same("A curious red fox likes to dance with a lantern.", Main.story(" red fox ", "dance", "curious", "a lantern"));
        same("A bright owl likes to read with a book.", Main.story("owl", "read", "bright", "a book"));
        if (args.length > 0) same("CURIOUS RED FOX ADVENTURE", Main.headline(" curious ", "red fox"));
        System.out.println("Review checks passed");
    }
}
