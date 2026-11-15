package journal;
import java.io.IOException;
import java.util.Objects;
/** Optional feature exercise: retry once only for a sink's no-commit IOException. */
public final class RetryingSink implements EntrySink {
    private final EntrySink delegate;
    public RetryingSink(EntrySink delegate) { this.delegate = Objects.requireNonNull(delegate, "delegate"); }
    public void write(Entry entry) throws IOException {
        // TODO: on IOException retry the same immutable entry once; preserve both failures.
        delegate.write(Objects.requireNonNull(entry, "entry"));
    }
}
