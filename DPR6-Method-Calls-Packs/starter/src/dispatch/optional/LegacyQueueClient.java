package dispatch.optional;
import dispatch.Quote;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public final class LegacyQueueClient {
    private LegacyQueueClient() {}
    public static Optional<String> printNext(LabelQueue queue) {
        Objects.requireNonNull(queue, "queue");
        try {
            Quote quote = queue.remove();
            return Optional.of(quote.label() + ":" + quote.cents());
        } catch (NoSuchElementException empty) {
            return Optional.empty();
        }
    }
}
