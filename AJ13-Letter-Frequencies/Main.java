import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            System.out.print("\nType in a sentence: ");
            String sentence = scanner.hasNextLine() ? scanner.nextLine() : "";
            Map<Integer, Integer> frequencies = new TreeMap<>();
            sentence.codePoints()
                    .filter(Character::isLetter)
                    .map(Character::toLowerCase)
                    .forEach(letter -> frequencies.merge(letter, 1, Integer::sum));

            if (frequencies.isEmpty()) {
                System.out.println("\nNo letters found.");
                return;
            }

            System.out.println("\nLetters: ");
            int mostFrequent = -1;
            int highestCount = 0;
            for (var entry : frequencies.entrySet()) {
                int letter = entry.getKey();
                int count = entry.getValue();
                System.out.printf("%s: %d%n", Character.toString(letter), count);
                if (count > highestCount) {
                    mostFrequent = letter;
                    highestCount = count;
                }
            }
            System.out.printf(
                    "%nThe most frequent letter is: %s%nWhich appears a whopping %d times!%n",
                    Character.toString(mostFrequent), highestCount);
        }
    }
}
