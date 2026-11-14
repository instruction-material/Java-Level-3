package dispatch.optional;
import dispatch.Quote;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Objects;

/** One owner, one thread; no concurrent consumers in this exercise. */
public final class LabelQueue {
    private final ArrayDeque<Quote> values = new ArrayDeque<>();
    public void add(Quote quote) { values.addLast(Objects.requireNonNull(quote, "quote")); }
    public boolean hasNext() { return !values.isEmpty(); }
    public Quote remove() {
        if (!hasNext()) throw new NoSuchElementException("empty queue");
        return values.removeFirst();
    }
    public int count() { return values.size(); }
}
