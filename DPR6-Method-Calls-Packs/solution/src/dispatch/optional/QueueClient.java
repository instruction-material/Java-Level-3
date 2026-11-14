package dispatch.optional;
import java.util.Objects;
import java.util.Optional;

public final class QueueClient {
    private QueueClient() {}
    public static Optional<String> printNext(LabelQueue queue) {
        Objects.requireNonNull(queue, "queue");
        if (!queue.hasNext()) return Optional.empty();
        var quote = queue.remove();
        return Optional.of(quote.label() + ":" + quote.cents());
    }
}
