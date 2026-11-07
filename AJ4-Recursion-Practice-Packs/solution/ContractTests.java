import java.math.BigInteger;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected, String label) {
        checks++;
        if (!actual.equals(expected)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try {
            action.run();
        } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Expected " + type.getSimpleName() + ", got " + error, error);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
    public static void main(String[] args) {
        int[] factorials = {1, 1, 2, 6, 24, 120, 720, 5040, 40320,
                362880, 3628800, 39916800, 479001600};
        for (int n = 0; n < factorials.length; n++) {
            equal(Main.factorial(n), factorials[n], "factorial " + n);
        }
        for (int n : new int[]{-1, 13, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            rejects(IllegalArgumentException.class, () -> Main.factorial(n));
        }
        int[] bases = {Integer.MIN_VALUE, -3, -2, -1, 0, 1, 2, 3, Integer.MAX_VALUE};
        for (int b : bases) {
            for (int power = 0; power <= 31; power++) {
                final int p = power;
                BigInteger expected = BigInteger.valueOf(b).pow(p);
                if (expected.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0
                        || expected.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    rejects(ArithmeticException.class, () -> Main.exponent(b, p));
                } else {
                    equal(Main.exponent(b, p), expected.intValueExact(), "power " + b + "," + p);
                }
            }
        }
        for (int p : new int[]{-1, 32, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            rejects(IllegalArgumentException.class, () -> Main.exponent(1, p));
        }
        for (int n : new int[]{0, 1, -1, 9, -9, 1234, -1234, 1000,
                Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            String digits = Long.toString(Math.abs((long) n));
            int expected = digits.chars().map(c -> c - '0').sum();
            equal(Main.sumDigits(n), expected, "digits " + n);
        }
        int a = 0;
        int b = 1;
        for (int term = 1; term <= 31; term++) {
            equal(Main.fibonacci(term), a, "one-based Fibonacci " + term);
            int next = a + b;
            a = b;
            b = next;
        }
        for (int n : new int[]{-1, 0, 32, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            rejects(IllegalArgumentException.class, () -> Main.fibonacci(n));
        }
        for (String s : new String[]{"", "a", "racecar", "abba", "ab", "Aba",
                "a a", "a!a", "abca", "x".repeat(256)}) {
            equal(Main.palindrome(s), new StringBuilder(s).reverse().toString().equals(s),
                    "palindrome " + s.length());
        }
        rejects(IllegalArgumentException.class, () -> Main.palindrome(null));
        rejects(IllegalArgumentException.class, () -> Main.palindrome("x".repeat(257)));
        rejects(IllegalArgumentException.class, () -> Main.palindrome("\u00e9"));
        System.out.println("Practice contracts: " + checks + " passed");
    }
}
