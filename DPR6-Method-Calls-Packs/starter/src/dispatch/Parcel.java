package dispatch;
import java.util.Objects;

/** Values crossing the modern API are validated in label/grams/zone order. */
public record Parcel(String label, int grams, int zone) {
    public Parcel {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        if (grams < 1 || grams > 5000) throw new IllegalArgumentException("grams");
        if (zone < 1 || zone > 3) throw new IllegalArgumentException("zone");
    }
}
