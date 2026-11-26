import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var stack = new java.util.ArrayDeque<Integer>();
        expect(true, Main.apply(stack, "-12"));
        expect(false, Main.apply(stack, "+"));
        expect(java.util.List.of(-12), java.util.List.copyOf(stack));
        expect(true, Main.apply(stack, "5"));
        expect(true, Main.apply(stack, "/"));
        expect(java.util.List.of(-2), java.util.List.copyOf(stack));
        expect(true, Main.apply(stack, "0"));
        expect(false, Main.apply(stack, "/"));
        expect(false, Main.apply(stack, "%"));
        expect(false, Main.apply(stack, "???"));
        expect(false, Main.apply(stack, "2147483648"));
        expect(java.util.List.of(-2, 0), java.util.List.copyOf(stack));
        expect(true, Main.apply(stack, "-"));
        expect(true, Main.apply(stack, "4"));
        expect(true, Main.apply(stack, "*"));
        expect(true, Main.apply(stack, "3"));
        expect(true, Main.apply(stack, "%"));
        expect(java.util.List.of(-2), java.util.List.copyOf(stack));
        System.out.println("Stack and queue checks passed");
    }
}
