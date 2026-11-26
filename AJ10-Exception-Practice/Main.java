import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        printFile(args.length == 0 ? "FAKE_FILE_NAME.txt" : args[0]);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Please enter an integer: ");
            if (!scanner.hasNext()) {
                System.out.println("ERROR: End of input");
            } else {
                try {
                    System.out.println("Integer: " + scanner.nextInt());
                } catch (InputMismatchException error) {
                    System.out.println("ERROR: You didn't enter an integer");
                }
            }
        }

        String word = null;
        if (startsWithVowel(word)) {
            System.out.println("The word starts with a vowel!");
        }

        try {
            System.out.println("Quotient: " + divide(50, 0));
        } catch (DivideByZeroException error) {
            System.out.println("ERROR: Tried to divide by zero");
        }
    }

    private static void printFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (FileNotFoundException error) {
            System.out.println("ERROR: Invalid filename");
        }
    }

    private static boolean startsWithVowel(String word) {
        if (word == null) {
            System.out.println("ERROR: The given word was null");
            return false;
        }
        return !word.isEmpty() && "aeiou".indexOf(Character.toLowerCase(word.charAt(0))) >= 0;
    }

    private static double divide(int numerator, int denominator) throws DivideByZeroException {
        if (denominator == 0) {
            throw new DivideByZeroException();
        }
        return (double) numerator / denominator;
    }
}
