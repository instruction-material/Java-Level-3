import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    static final class DivideByZeroException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    static int parse(String token) {
        // TODO: Parse a signed integer and report missing data.
        throw new UnsupportedOperationException("Parse a signed integer and report missing data");
    }

    static boolean startsWithVowel(String word) {
        // TODO: Handle absent text before testing a vowel.
        throw new UnsupportedOperationException("Handle absent text before testing a vowel");
    }

    static int wordCount(Path path) throws IOException {
        // TODO: Read UTF-8 and count whitespace-delimited words.
        throw new UnsupportedOperationException("Read UTF-8 and count whitespace-delimited words");
    }

    static double divide(int numerator, int denominator) throws DivideByZeroException {
        // TODO: Divide without truncation and reject zero.
        throw new UnsupportedOperationException("Divide without truncation and reject zero");
    }

    public static void main(String[] args) {
        try {
            int number = parse(args.length > 0 ? args[0] : "5");
            int divisor = parse(args.length > 1 ? args[1] : "2");
            System.out.println("Quotient: " + divide(number, divisor));
        } catch (NumberFormatException error) {
            System.out.println("Invalid integer.");
        } catch (DivideByZeroException error) {
            System.out.println("Cannot divide by zero.");
        }
        try {
            System.out.println("Words: " + wordCount(Path.of(args.length > 2 ? args[2] : "sample.txt")));
        } catch (IOException error) {
            System.out.println("Cannot read the file.");
        }
        System.out.println("Apple starts with a vowel: " + startsWithVowel("Apple"));
    }
}
