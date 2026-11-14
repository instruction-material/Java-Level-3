package dispatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** A single-threaded log shared across immutable service configurations. */
public final class DispatchLog {
    private final List<Quote> sent = new ArrayList<>();
    void append(Quote quote) { sent.add(Objects.requireNonNull(quote, "quote")); }
    public List<Quote> snapshot() { return List.copyOf(sent); }
    public int totalCents() { return sent.stream().mapToInt(Quote::cents).sum(); }
    public Optional<Quote> find(String label) {
        Objects.requireNonNull(label, "label");
        if (label.isBlank()) throw new IllegalArgumentException("label");
        for (int i = sent.size() - 1; i >= 0; i--)
            if (sent.get(i).label().equals(label)) return Optional.of(sent.get(i));
        return Optional.empty();
    }
}
