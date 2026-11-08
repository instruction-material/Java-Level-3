package wrapping.legacy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import wrapping.Source;

/** Preserved baseline combines counting, caching and text transformations. */
public final class LegacyReader {
    private final Source source;
    private final String prefix;
    private final boolean upper;
    private final boolean cached;
    private final Map<String, String> values = new HashMap<>();
    private long attempts;

    public LegacyReader(Source source, String prefix, boolean upper, boolean cached) {
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
}
