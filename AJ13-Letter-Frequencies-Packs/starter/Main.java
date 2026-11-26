import java.util.OptionalInt;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    static SortedMap<Integer, Integer> frequencies(String text) {
        // TODO: Count normalized letter code points.
        throw new UnsupportedOperationException("Count normalized letter code points");
    }

    static OptionalInt winner(SortedMap<Integer, Integer> counts) {
        // TODO: Find the highest count with the first tied key.
        throw new UnsupportedOperationException("Find the highest count with the first tied key");
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
