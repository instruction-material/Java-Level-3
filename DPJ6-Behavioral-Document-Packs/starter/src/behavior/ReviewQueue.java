// Iterator: each traversal owns a position in an immutable membership snapshot.
package behavior;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public final class ReviewQueue implements Iterable<String> {
    private final List<String> entries = new ArrayList<>();
    public void add(String text) { entries.add(TextRule.line(text)); }
    public int size() { return entries.size(); }
    public Iterator<String> iterator() {
        throw new UnsupportedOperationException("Implement an independent snapshot iterator.");
    }
    public List<String> matching(Predicate<String> predicate) {
        throw new UnsupportedOperationException("Implement optional snapshot selection.");
    }
}
