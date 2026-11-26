import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    static final class DivideByZeroException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    static int parse(String token) {
        if (token == null) throw new NumberFormatException("Missing integer.");
        return Integer.parseInt(token.strip());
    }

    static boolean startsWithVowel(String word) {
        return word != null && !word.isEmpty() &&
                "aeiou".indexOf(Character.toLowerCase(word.charAt(0))) >= 0;
    }

    static int wordCount(Path path) throws IOException {
        String text = Files.readString(path).strip();
        return text.isEmpty() ? 0 : text.split("\\p{javaWhitespace}+").length;
    }

    static double divide(int numerator, int denominator) throws DivideByZeroException {
        if (denominator == 0) throw new DivideByZeroException();
        return (double) numerator / denominator;
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
