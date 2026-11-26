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
        var sample = new java.util.ArrayList<>(List.of("zebra", "apple", "Apple", "!yes", "apple"));
        Main.sort(sample);
        expect(List.of("!yes", "Apple", "apple", "apple", "zebra"), sample);
        Main.sort(new java.util.ArrayList<String>());
        try {
            var loaded = Main.readWords(java.nio.file.Path.of("words.txt"));
            var expected = new java.util.ArrayList<>(loaded);
            expected.sort(String::compareTo);
            Main.sort(loaded);
            expect(expected, loaded);
        } catch (java.io.IOException error) {
            throw new AssertionError("Expected the bundled word list", error);
        }
        System.out.println("Sorting checks passed");
    }
}
