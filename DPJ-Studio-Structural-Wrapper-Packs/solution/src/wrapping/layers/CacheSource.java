package wrapping.layers;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import wrapping.Source;

public final class CacheSource implements Source {
    private final Source delegate;
    private final Map<String, String> values = new HashMap<>();
    public CacheSource(Source delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }
    @Override
    public String load(String key) {
        String value = values.get(key);
        if (value == null) {
            value = Objects.requireNonNull(delegate.load(key), "source result");
            values.put(key, value);
        }
        return value;
    }
    public void clear() {
        values.clear();
    }
    public boolean invalidate(String key) {
        return values.remove(key) != null;
    }
}
