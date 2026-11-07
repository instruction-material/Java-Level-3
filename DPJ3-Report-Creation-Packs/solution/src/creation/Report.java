package creation;
import java.util.List;
import java.util.Objects;

/** Immutable single-line title and snapshot of immutable body strings. */
public record Report(String title, List<String> lines) {
    public Report {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(lines, "lines");
        if (title.isBlank() || title.contains("\n") || title.contains("\r"))
            throw new IllegalArgumentException("title must be a nonblank single line");
        for (String line : lines) {
            Objects.requireNonNull(line, "line");
            if (line.contains("\n") || line.contains("\r"))
                throw new IllegalArgumentException("each body item must be a single line");
        }
        lines = List.copyOf(lines);
    }
}
