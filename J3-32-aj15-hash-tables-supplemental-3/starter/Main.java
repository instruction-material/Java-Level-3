import java.util.ArrayList;
import java.util.List;

public class Main {
    record Entry(int key, String value) {}

    static List<List<Entry>> resize(List<List<Entry>> oldBuckets, int capacity) {
        // TODO: Create new buckets and reindex every entry.
        throw new UnsupportedOperationException("Create new buckets and reindex every entry");
    }

    public static void main(String[] args) {
        var entries = List.of(List.of(new Entry(2, "two"), new Entry(5, "five"), new Entry(-1, "negative")));
        var resized = resize(entries, 3);
        System.out.println(resized.get(2).size());
        System.out.println(resize(resized, 5).get(0));
    }
}
