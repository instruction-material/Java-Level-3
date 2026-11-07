package notes.domain;
import java.util.Objects;
public record Note(String id, String text) {
    public Note { id=key(id); text=line(text); }
    public static String key(String value) {
        Objects.requireNonNull(value,"id");
        if (!value.matches("[a-z][a-z0-9-]*")) throw new IllegalArgumentException("invalid id");
        return value;
    }
    public static String line(String value) {
        Objects.requireNonNull(value,"text");
        if (value.indexOf('\r')>=0 || value.indexOf('\n')>=0) throw new IllegalArgumentException("single-line text required");
        return value;
    }
}
