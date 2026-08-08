import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) {

        //#1
        printFile("FAKE_FILE_NAME.txt");

        //#2
        int integer = getInteger();

        //#3
        String word = null;
        if (startsWithVowel(word)) {
            System.out.println("The word starts with a vowel!");
        }

        //#4
        double quotient = divide(50, 0);
    }

    private static void printFile(String fileName) {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
    }

    private static int getInteger() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter an integer: ");
        int word = scanner.nextInt();
        return word;
    }

    private static boolean startsWithVowel(String word) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        if (word.length() == 0) {
            return false;
        }
        for (char vowel : vowels) {
            if (word.charAt(0) == vowel) {
                return true;
            }
        }
        return false;
    }

    private static double divide(int num1, int num2) {
        double answer = num1 / num2;
        return answer;
    }
}
