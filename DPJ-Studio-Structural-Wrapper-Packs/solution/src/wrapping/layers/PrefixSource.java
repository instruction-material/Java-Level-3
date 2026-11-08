package wrapping.layers;
import java.util.Objects;
import wrapping.Source;

public final class PrefixSource implements Source {
    private final Source delegate;
    private final String prefix;
    public PrefixSource(Source delegate, String prefix) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.prefix = Objects.requireNonNull(prefix, "prefix");
    }
    @Override
    public String load(String key) {
        return prefix + Objects.requireNonNull(delegate.load(key), "source result");
    }
}
