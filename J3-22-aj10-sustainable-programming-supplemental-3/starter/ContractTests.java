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
        var result = Main.summarize(new String[]{"12.5", "bad", "NaN", "-40", "61", "60", null});
        expect(List.of(12.5, -40.0, 60.0), result.values());
        expect(List.of(2, 3, 5, 7), result.invalidLines());
        expect(new Main.Report(List.of(), List.of()), Main.summarize(new String[0]));
        expect(List.of(1), Main.summarize(new String[]{"Infinity"}).invalidLines());
        System.out.println("Sustainable-programming checks passed");
    }
}
