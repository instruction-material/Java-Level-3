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
        List<String> snapshot = List.copyOf(entries);
        return new Iterator<>() {
            private int index;
            public boolean hasNext() { return index < snapshot.size(); }
            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                return snapshot.get(index++);
            }
        };
    }
    public List<String> matching(Predicate<String> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        List<String> snapshot = List.copyOf(entries), result = new ArrayList<>();
        for (String entry : snapshot) if (predicate.test(entry)) result.add(entry);
        return List.copyOf(result);
    }
}
