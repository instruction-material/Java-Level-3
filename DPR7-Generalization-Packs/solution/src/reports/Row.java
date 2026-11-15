package reports;
import java.util.Objects;
public record Row(String label, int count) {
    public Row {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        if (count < 0 || count > 50) throw new IllegalArgumentException("count");
    }
}
