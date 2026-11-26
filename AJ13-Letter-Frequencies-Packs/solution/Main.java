import java.util.OptionalInt;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    static SortedMap<Integer, Integer> frequencies(String text) {
        var counts = new TreeMap<Integer, Integer>();
        text.codePoints().filter(Character::isLetter).map(Character::toLowerCase)
                .forEach(letter -> counts.merge(letter, 1, Integer::sum));
        return counts;
    }

    static OptionalInt winner(SortedMap<Integer, Integer> counts) {
        int maximum = 0;
        int first = Integer.MAX_VALUE;
        for (var entry : counts.entrySet()) {
            int letter = entry.getKey();
            int count = entry.getValue();
            if (count > maximum || (count == maximum && letter < first)) {
                first = letter;
                maximum = count;
            }
        }
        return maximum == 0 ? OptionalInt.empty() : OptionalInt.of(first);
    }

    public static void main(String[] args) {
        var counts = frequencies(args.length == 0 ? "Baa BB! café É" : String.join(" ", args));
        for (var entry : counts.entrySet()) {
            System.out.println(Character.toString(entry.getKey()) + ": " + entry.getValue());
        }
        var mostFrequent = winner(counts);
        if (mostFrequent.isPresent()) {
            System.out.println("Most frequent: " + Character.toString(mostFrequent.getAsInt()));
        } else {
            System.out.println("No letters found.");
        }
    }
}
