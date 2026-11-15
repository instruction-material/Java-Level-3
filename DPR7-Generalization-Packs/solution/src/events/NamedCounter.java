package events;
import java.util.Objects;
/** Adds metadata while inheriting the entire unchanged Counter contract. */
public final class NamedCounter extends Counter implements NamedValue {
    private final String label;
    public NamedCounter(String label) {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        this.label = label;
    }
    public String label() { return label; }
}
