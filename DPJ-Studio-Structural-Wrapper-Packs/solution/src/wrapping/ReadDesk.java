package wrapping;

import java.util.Objects;
import wrapping.layers.CacheSource;
import wrapping.layers.CountingSource;
import wrapping.layers.PrefixSource;
import wrapping.layers.UpperSource;

/** Composition preserves the existing ReadDesk client contract. */
public final class ReadDesk {
    private final CacheSource cache;
    private final CountingSource counter;

    public ReadDesk(Source source, String prefix, boolean upper, boolean cached) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(prefix, "prefix");
        Source pipeline = source;
        if (upper) pipeline = new UpperSource(pipeline);
        pipeline = new PrefixSource(pipeline, prefix);
        cache = cached ? new CacheSource(pipeline) : null;
        counter = new CountingSource(cache == null ? pipeline : cache);
    }

    public String read(String key) {
        Source.validKey(key);
        return counter.load(key);
    }

    public long attempts() {
        return counter.attempts();
    }

    public void clearCache() {
        if (cache != null) cache.clear();
    }

    public boolean invalidate(String key) {
        Source.validKey(key);
        return cache != null && cache.invalidate(key);
    }
}
