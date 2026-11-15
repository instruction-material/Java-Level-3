package journal;
import java.time.Instant;
import java.util.Objects;
public final class CaptureNormalizer {
    private CaptureNormalizer() {}
    public static String normalize(String line) {
        Objects.requireNonNull(line, "line");
        if (!line.endsWith("\n") || line.indexOf('\n') != line.length() - 1 || line.indexOf('\r') >= 0)
            throw new IllegalArgumentException("line");
        String[] parts = line.substring(0, line.length() - 1).split("\\|", -1);
        if (parts.length != 4 || !parts[1].matches("0|[1-9][0-9]{0,5}") || !parts[3].matches("0|[1-9][0-9]?"))
            throw new IllegalArgumentException("line");
        try { new Entry(Instant.parse(parts[0]), Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3])); }
        catch (RuntimeException error) { throw new IllegalArgumentException("line", error); }
        return "<time>|<id>|" + parts[2] + "|" + parts[3] + "\n";
    }
}
