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
        same("[[0, 1, 2], [1, 2, 3]]", Arrays.deepToString(Main.grid(2, 3)));
        same(OptionalInt.of(-3), Main.maximum(new int[][]{{}, {-5}, {-3, -9}}));
        same(OptionalInt.empty(), Main.maximum(Main.grid(3, 0)));
        invalid(() -> Main.grid(-1, 4));
        same(true, Main.contains(new int[][]{{0}, {}, {4, 8}}, 8));
        same(true, Main.contains(new int[][]{{0}, {}, {4, 8}}, 0));
        same(false, Main.contains(new int[][]{{0}, {}, {4, 8}}, 6));
        double[][] sample = Main.randomGrid(2, 3, new Random(2));
        same(2, sample.length);
        same(3, sample[1].length);
        same(true, Arrays.stream(sample).flatMapToDouble(Arrays::stream).allMatch(value -> value >= 0 && value < 10));
        same(Arrays.deepToString(sample), Arrays.deepToString(Main.randomGrid(2, 3, new Random(2))));
        System.out.println("Review checks passed");
    }
}
