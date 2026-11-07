import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    private static String line(String value) {
        Objects.requireNonNull(value, "line");
        if (value.contains("\n") || value.contains("\r"))
            throw new IllegalArgumentException("line must not contain CR or LF");
        return value;
    }
    public record Note(String title, List<String> lines) {
        public Note {
            title = line(title);
            if (title.isBlank()) throw new IllegalArgumentException("title must not be blank");
            lines = List.copyOf(lines);
            for (String item : lines) line(item);
        }
    }
    public static final class Draft {
        private final String title;
        private final List<String> lines;
        public Draft(String title, List<String> source) {
            Note valid = new Note(title, source);
            this.title = valid.title();
            lines = new ArrayList<>(valid.lines());
        }
        public void add(String text) { lines.add(line(text)); }
        public Draft copy() { return new Draft(title, lines); }
        public Note snapshot() { return new Note(title, lines); }
    }
    @FunctionalInterface
    public interface Legacy { String fetchPipeText(); }
    @FunctionalInterface
    public interface NoteSource { Note load(); }
    public static final class Adapter implements NoteSource {
        private final Legacy legacy;
        public Adapter(Legacy legacy) { this.legacy = Objects.requireNonNull(legacy, "legacy"); }
        public Note load() {
            String text = Objects.requireNonNull(legacy.fetchPipeText(), "legacy text");
            String[] fields = text.split("\\|", -1);
            return new Note(fields[0], Arrays.asList(Arrays.copyOfRange(fields, 1, fields.length)));
        }
    }
    public enum LineEnding {
        INSTANCE;
        public String value() { return "\n"; }
    }
    public static final class Facade {
        private final NoteSource source;
        public Facade(NoteSource source) { this.source = Objects.requireNonNull(source, "source"); }
        public String render() {
            Note note = Objects.requireNonNull(source.load(), "note");
            StringBuilder text = new StringBuilder(note.title()).append(LineEnding.INSTANCE.value());
            for (String body : note.lines()) text.append(body).append(LineEnding.INSTANCE.value());
            return text.toString();
        }
    }
    public static void main(String[] args) {
        Draft original = new Draft("T", List.of("Ready"));
        Draft copy = original.copy(); copy.add("Next");
        System.out.println(original.snapshot().lines().size());
        System.out.println(copy.snapshot().lines().size());
        System.out.print(new Facade(new Adapter(() -> "T|Ready|Done")).render());
    }
}
