import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        expect(true, Main.isBalanced(""));
        expect(true, Main.isBalanced("{[()]}"));
        expect(true, Main.isBalanced("[](){}"));
        for (String invalid : new String[]{null, "([)]", "(()", "{}x", "]"}) {
            expect(false, Main.isBalanced(invalid));
        }
        expect(true, Main.isBalanced("()"));
        expect(true, Main.isBalanced("(".repeat(1200) + ")".repeat(1200)));
        expect(false, Main.isBalanced("(".repeat(1200) + ")".repeat(999)));
        System.out.println("Stack and queue checks passed");
    }
}
