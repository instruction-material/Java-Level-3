package wrapping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Runnable core starter for a client-preserving wrapper refactor. */
public final class ReadDesk {
    private final Source source;
    private final String prefix;
    private final boolean upper;
    private final boolean cached;
    private final Map<String, String> values = new HashMap<>();
    private long attempts;

    public ReadDesk(Source source, String prefix, boolean upper, boolean cached) {
        this.source = Objects.requireNonNull(source, "source");
        this.prefix = Objects.requireNonNull(prefix, "prefix");
        this.upper = upper;
        this.cached = cached;
    }

    public String read(String key) {
        Source.validKey(key);
        attempts++;
        if (cached && values.containsKey(key)) return values.get(key);
        String value = Objects.requireNonNull(source.load(key), "source result");
        if (upper) value = value.toUpperCase(Locale.ROOT);
        value = prefix + value;
        if (cached) values.put(key, value);
        return value;
    }

    public long attempts() {
        return attempts;
    }

    public void clearCache() {
        values.clear();
    }

    public boolean invalidate(String key) {
        throw new UnsupportedOperationException("Implement optional selective invalidation.");
    }
}
