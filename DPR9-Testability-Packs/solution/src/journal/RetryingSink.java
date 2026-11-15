package journal;
import java.io.IOException;
import java.util.Objects;
public final class RetryingSink implements EntrySink {
    private final EntrySink delegate;
    public RetryingSink(EntrySink delegate) { this.delegate = Objects.requireNonNull(delegate, "delegate"); }
    public void write(Entry entry) throws IOException {
        Objects.requireNonNull(entry, "entry");
        try { delegate.write(entry); }
        catch (IOException first) {
            try { delegate.write(entry); }
            catch (IOException second) {
                if (second != first) second.addSuppressed(first);
                throw second;
            }
        }
    }
}
