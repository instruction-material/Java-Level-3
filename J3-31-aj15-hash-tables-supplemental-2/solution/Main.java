import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class Main {
    record Entry(int key, int value) {}

    static List<List<Entry>> table(int capacity) {
        var buckets = new ArrayList<List<Entry>>();
        for (int i = 0; i < capacity; i++) buckets.add(new ArrayList<>());
        return buckets;
    }

    static void put(List<List<Entry>> buckets, int key, int value) {
        var bucket = buckets.get(Math.floorMod(key, buckets.size()));
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).key() == key) {
                bucket.set(i, new Entry(key, value));
                return;
            }
        }
        bucket.add(new Entry(key, value));
    }

    static OptionalInt get(List<List<Entry>> buckets, int key) {
        for (Entry entry : buckets.get(Math.floorMod(key, buckets.size()))) {
            if (entry.key() == key) return OptionalInt.of(entry.value());
        }
        return OptionalInt.empty();
    }

    public static void main(String[] args) {
        var buckets = table(3);
        put(buckets, -1, 8);
        put(buckets, 2, 9);
        put(buckets, -1, 7);
        System.out.println(get(buckets, -1).orElseThrow());
        System.out.println(get(buckets, 2).orElseThrow());
        System.out.println(get(buckets, 5));
    }
}
