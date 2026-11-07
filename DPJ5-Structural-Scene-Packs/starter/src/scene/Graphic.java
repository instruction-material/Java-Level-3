package scene;
import java.util.Objects;

@FunctionalInterface
public interface Graphic {
    String render();
    static String name(String value) {
        Objects.requireNonNull(value, "name");
        if (!value.matches("[A-Za-z][A-Za-z0-9_]{0,19}")) throw new IllegalArgumentException("invalid name");
        return value;
    }
    static int size(int value) {
        if (value < 1 || value > 100) throw new IllegalArgumentException("size must be 1..100");
        return value;
    }
}
