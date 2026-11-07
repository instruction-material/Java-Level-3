package report.legacy;
import java.util.List;
import java.util.Objects;

/** Before-refactor example: validation, formatting and output share one class. */
public final class ReportBlob {
    private ReportBlob() {}
    public static String render(String title, List<String> lines) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(lines, "lines");
        if (title.isBlank() || title.contains("\n") || title.contains("\r"))
            throw new IllegalArgumentException("invalid title");
        StringBuilder text = new StringBuilder(title).append('\n');
        for (String line : lines) {
            Objects.requireNonNull(line, "line");
            if (line.contains("\n") || line.contains("\r"))
                throw new IllegalArgumentException("invalid body line");
            text.append(line).append('\n');
        }
        return text.toString();
    }
    public static void print(String title, List<String> lines) {
        System.out.print(render(title, lines));
    }
}
