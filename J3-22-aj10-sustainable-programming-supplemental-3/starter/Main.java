import java.util.ArrayList;
import java.util.List;

public class Main {
    record Report(List<Double> values, List<Integer> invalidLines) {}

    static double read(String line) {
        if (line == null) throw new NumberFormatException("Missing value.");
        double value = Double.parseDouble(line.strip());
        if (!Double.isFinite(value) || value < -40 || value > 60) {
            throw new IllegalArgumentException("Reading is outside the finite supported range.");
        }
        return value;
    }

    static Report summarize(String[] lines) {
        // TODO: Collect successful values and continue after rejected rows.
        throw new UnsupportedOperationException("Collect successful values and continue after rejected rows");
    }

    public static void main(String[] args) {
        Report result = summarize(new String[]{"12.5", "bad", "NaN", "-40", "61", "60"});
        System.out.println(result.values());
        System.out.println(result.invalidLines());
    }
}
