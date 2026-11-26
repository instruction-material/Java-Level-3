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
        var ingredients = new ArrayList<>(List.of("flour", "water"));
        var small = new Main.Bread("Small", ingredients, Main.Size.SMALL);
        var medium = new Main.Bread("Medium", List.of("flour"), Main.Size.MEDIUM);
        var large = new Main.Bread("Large", List.of(), Main.Size.LARGE);
        ingredients.clear();
        same(2, small.ingredientCount());
        same(true, small.compareTo(medium) > 0);
        same(250, small.priceCents());
        same(350, medium.priceCents());
        same(450, large.priceCents());
        System.out.println("Review checks passed");
    }
}
