package notices;
import java.util.Objects;
public record Notice(String label, int count) {
    public Notice {
        Objects.requireNonNull(label, "label");
        if (label.isBlank() || label.indexOf('\n') >= 0 || label.indexOf('\r') >= 0)
            throw new IllegalArgumentException("label");
        if (count < 0 || count > 9) throw new IllegalArgumentException("count");
    }
}
