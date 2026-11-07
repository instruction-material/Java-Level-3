package behavior;
import java.util.Objects;

@FunctionalInterface
public interface TextRule {
    String apply(String text);
    static String line(String value) {
        Objects.requireNonNull(value, "text");
        if (value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0)
            throw new IllegalArgumentException("text must be a single line");
        return value;
    }
}
