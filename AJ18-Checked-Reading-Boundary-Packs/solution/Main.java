import java.util.ArrayList;
import java.util.List;

class Main {
    static class ReadingError extends Exception {
        private static final long serialVersionUID = 1L;
        ReadingError(String message) { super(message); }
    }

    record Batch(List<Integer> readings, List<Integer> invalidLines) {}

    static int parse(String text) throws ReadingError {
        int value;
        try {
            value = Integer.parseInt(text.strip());
        } catch (NumberFormatException invalid) {
            throw new ReadingError("Expected an integer");
        }
        if (value < 0 || value > 100) throw new ReadingError("Expected a value from 0 to 100");
        return value;
    }

    static Batch collect(List<String> input) {
        var readings = new ArrayList<Integer>();
        var invalid = new ArrayList<Integer>();
        for (int index = 0; index < input.size(); index++) {
            try {
                readings.add(parse(input.get(index)));
            } catch (ReadingError error) {
                invalid.add(index + 1);
            }
        }
        return new Batch(List.copyOf(readings), List.copyOf(invalid));
    }

    public static void main(String[] args) {
        Batch batch = collect(List.of("8", "bad", " 12 ", "101", "0"));
        System.out.println(batch.readings());
        System.out.println(batch.invalidLines());
    }
}
