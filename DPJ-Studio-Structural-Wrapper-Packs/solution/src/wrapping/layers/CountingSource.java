package wrapping.layers;
import java.util.Objects;
import wrapping.Source;

public final class CountingSource implements Source {
    private final Source delegate;
    private long attempts;
    public CountingSource(Source delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }
    @Override
    public String load(String key) {
        attempts++;
        return Objects.requireNonNull(delegate.load(key), "source result");
    }
    public long attempts() {
        return attempts;
    }
}
