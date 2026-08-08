import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) {

        System.out.println();

        //#1
        printFile("FAKE_FILE_NAME.txt");

        //#2
        try {
            int integer = getInteger();
        } catch (InputMismatchException ex) {
            System.out.println("ERROR: You didn't enter an integer");
        }

        //#3
        String word = null;
        if (startsWithVowel(word)) {
            System.out.println("The word starts with a vowel!");
        }

        //#4
        try {
            double quotient = divide(50, 0);
        } catch (DivideByZeroException ex) {
            System.out.println("ERROR: Tried to divide by zero");
        }
    }

    private static void printFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            ArrayList<String> words = new ArrayList<>();
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Invalid filename");
        }
    }

    private static int getInteger() throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter an integer: ");
        int word = scanner.nextInt();
        return word;
    }

    private static boolean startsWithVowel(String word) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        try {
            if (word.length() == 0) {
                return false;
            }
            for (char vowel : vowels) {
                if (word.charAt(0) == vowel) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException ex) {
            System.out.println("ERROR: The given word was null");
            return false;
        }
    }

    private static double divide(int num1, int num2)
        throws DivideByZeroException {
        if (num2 == 0) {
            throw new DivideByZeroException();
        }
        double answer = num1 / num2;
        return answer;
    }
}
