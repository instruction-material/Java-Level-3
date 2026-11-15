package journal;
import java.time.Instant;
import java.util.Objects;
public record Entry(Instant when, int id, String label, int count) {
    public Entry {
        Objects.requireNonNull(when, "when");
        if (id < 0 || id >= 1_000_000) throw new IllegalArgumentException("id");
        validate(label, count);
    }
    public static void validate(String label, int count) {
        Objects.requireNonNull(label, "label");
        if (label.isBlank() || label.indexOf('|') >= 0 || label.indexOf('\n') >= 0 || label.indexOf('\r') >= 0)
            throw new IllegalArgumentException("label");
        if (count < 0 || count > 99) throw new IllegalArgumentException("count");
    }
    public String line() { return when + "|" + id + "|" + label + "|" + count + "\n"; }
}
