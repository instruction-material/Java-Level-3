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
        // TODO: Update or append a key inside its collision chain.
        throw new UnsupportedOperationException("Update or append a key inside its collision chain");
    }

    static OptionalInt get(List<List<Entry>> buckets, int key) {
        // TODO: Look for an equal key inside its collision chain.
        throw new UnsupportedOperationException("Look for an equal key inside its collision chain");
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
