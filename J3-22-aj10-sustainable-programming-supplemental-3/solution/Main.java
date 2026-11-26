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
        List<Double> values = new ArrayList<>();
        List<Integer> invalid = new ArrayList<>();
        for (int index = 0; index < lines.length; index++) {
            try {
                values.add(read(lines[index]));
            } catch (IllegalArgumentException error) {
                invalid.add(index + 1);
            }
        }
        return new Report(List.copyOf(values), List.copyOf(invalid));
    }

    public static void main(String[] args) {
        Report result = summarize(new String[]{"12.5", "bad", "NaN", "-40", "61", "60"});
        System.out.println(result.values());
        System.out.println(result.invalidLines());
    }
}
