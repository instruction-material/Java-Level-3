import java.util.ArrayList;
import java.util.List;

public class Main {
    record Entry(int key, String value) {}

    static List<List<Entry>> resize(List<List<Entry>> oldBuckets, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        var buckets = new ArrayList<List<Entry>>(capacity);
        for (int i = 0; i < capacity; i++) buckets.add(new ArrayList<>());
        for (var bucket : oldBuckets) {
            for (var entry : bucket) buckets.get(Math.floorMod(entry.key(), capacity)).add(entry);
        }
        return buckets;
    }

    public static void main(String[] args) {
        var entries = List.of(List.of(new Entry(2, "two"), new Entry(5, "five"), new Entry(-1, "negative")));
        var resized = resize(entries, 3);
        System.out.println(resized.get(2).size());
        System.out.println(resize(resized, 5).get(0));
    }
}
