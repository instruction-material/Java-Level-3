import java.util.ArrayList;
import java.util.List;

class Main {
    static class ReadingError extends Exception {
        private static final long serialVersionUID = 1L;
        ReadingError(String message) { super(message); }
    }

    record Batch(List<Integer> readings, List<Integer> invalidLines) {}

    static int parse(String text) throws ReadingError {
        // TODO: Classify invalid measurements at the parser boundary.
        throw new UnsupportedOperationException("Classify invalid measurements at the parser boundary");
    }

    static Batch collect(List<String> input) {
        // TODO: Recover for each invalid line and continue.
        throw new UnsupportedOperationException("Recover for each invalid line and continue");
    }

    public static void main(String[] args) {
        Batch batch = collect(List.of("8", "bad", " 12 ", "101", "0"));
        System.out.println(batch.readings());
        System.out.println(batch.invalidLines());
    }
}
