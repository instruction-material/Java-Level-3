package dispatch;
import java.util.Objects;
public record Quote(String label, int cents) {
    public Quote {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        if (cents < 0) throw new IllegalArgumentException("cents");
    }
}
