package wrapping;

import java.util.Objects;

/** Sequential text lookup: callers supply a validated key; successful results are non-null. */
@FunctionalInterface
public interface Source {
    String load(String key);

    static void validKey(String key) {
        Objects.requireNonNull(key, "key");
        if (!key.matches("[a-z][a-z0-9-]{0,15}"))
            throw new IllegalArgumentException("key");
    }
}
