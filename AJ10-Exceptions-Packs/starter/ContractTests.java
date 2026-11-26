import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;

public class ContractTests {

    static void expect(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void expectArray(int[] expected, int[] actual) {
        expect(Arrays.toString(expected), Arrays.toString(actual));
    }

    public static void main(String[] args) {
        expect(17, Main.parse(" +17 "));
        expect(Integer.MIN_VALUE, Main.parse("-2147483648"));
        for (String invalid : new String[]{null, "", "17.5", "2147483648"}) {
            try {
                Main.parse(invalid);
                throw new AssertionError("Accepted invalid input");
            } catch (NumberFormatException expected) {
                // The console handles this expected error.
            }
        }
        expect(true, Main.startsWithVowel("Apple"));
        expect(false, Main.startsWithVowel(null));
        expect(false, Main.startsWithVowel(""));
        expect(false, Main.startsWithVowel("pear"));
        try {
            expect(2.5, Main.divide(5, 2));
            expect(-2.5, Main.divide(-5, 2));
            try {
                Main.divide(1, 0);
                throw new AssertionError("Zero denominator");
            } catch (Main.DivideByZeroException expected) {
                // A zero denominator uses the documented checked exception.
            }
        } catch (Main.DivideByZeroException error) {
            throw new AssertionError("Nonzero denominator rejected", error);
        }
        try {
            expect(3, Main.wordCount(java.nio.file.Path.of("sample.txt")));
            try {
                Main.wordCount(java.nio.file.Path.of("missing-course-file.txt"));
                throw new AssertionError("Missing file accepted");
            } catch (java.nio.file.NoSuchFileException expected) {
                // File errors reach their caller.
            }
        } catch (java.io.IOException error) {
            throw new AssertionError("Sample file was not readable", error);
        }
        System.out.println("Sustainable-programming checks passed");
    }
}
