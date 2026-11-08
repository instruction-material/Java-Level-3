package wrapping.layers;
import java.util.Locale;
import java.util.Objects;
import wrapping.Source;

public final class UpperSource implements Source {
    private final Source delegate;
    public UpperSource(Source delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }
    @Override
    public String load(String key) {
        return Objects.requireNonNull(delegate.load(key), "source result").toUpperCase(Locale.ROOT);
    }
}
