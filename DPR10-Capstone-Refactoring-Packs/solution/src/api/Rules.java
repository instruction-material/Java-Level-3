package api;
import java.util.Objects;
public final class Rules {
    private Rules() {}
    public static String id(String value, String label) {
        Objects.requireNonNull(value, label);
        if (!value.matches("[a-z][a-z0-9-]{0,15}")) throw new IllegalArgumentException(label);
        return value;
    }
    public static int capacity(int value) {
        if (value < 1 || value > 4) throw new IllegalArgumentException("capacity");
        return value;
    }
    public static String style(String value) {
        Objects.requireNonNull(value, "style");
        if (!value.equals("plain") && !value.equals("compact")) throw new IllegalArgumentException("style");
        return value;
    }
}
