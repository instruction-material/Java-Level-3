import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nType in a sentence: ");
        String sentence = scanner.nextLine();

        Map<Character, Integer> frequencies = new TreeMap<>();

        // fill the map
        for (int i = 0; i < sentence.length(); i++) {
            char letter = sentence.charAt(i);
            letter = Character.toLowerCase(letter);
            if (letter != ' ') {
                int frequency = 1;
                if (frequencies.containsKey(letter)) {
                    frequency += frequencies.get(letter);
                }
                frequencies.put(letter, frequency);
            }
        }

        // print all the letters
        System.out.println("\nLetters: ");
        for (char letter : frequencies.keySet()) {
            System.out.print(letter + ": ");
            System.out.print(frequencies.get(letter) + "\n");
        }

        // find the maximum letter
        char maximumLetter = frequencies.keySet().iterator().next();
        for (char letter : frequencies.keySet()) {
            if (frequencies.get(letter) > frequencies.get(maximumLetter)) {
                maximumLetter = letter;
            }
        }
        System.out.print("\nThe most frequent letter is: " + maximumLetter +
                         "\nWhich appears a whopping " +
                         frequencies.get(maximumLetter) + " times!");
    }
}
