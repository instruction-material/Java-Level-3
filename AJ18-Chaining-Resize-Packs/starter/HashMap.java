import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashMap<K, V> {
    private static final class Entry<K, V> {
        private final K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private List<LinkedList<Entry<K, V>>> buckets = emptyBuckets(47);
    private int size;

    private List<LinkedList<Entry<K, V>>> emptyBuckets(int capacity) {
        var result = new ArrayList<LinkedList<Entry<K, V>>>(capacity);
        for (int index = 0; index < capacity; index++) {
            result.add(new LinkedList<>());
        }
        return result;
    }

    private int index(K key, int capacity) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    private LinkedList<Entry<K, V>> bucket(K key) {
        return buckets.get(index(key, buckets.size()));
    }

    private Entry<K, V> find(K key) {
        for (Entry<K, V> entry : bucket(key)) {
            if (entry.key.equals(key)) return entry;
        }
        return null;
    }

    private void addNew(K key, V value) {
        bucket(key).add(new Entry<>(key, value));
        size++;
        if (size > buckets.size() / 2) resize();
    }

    // Keep the original value when the key is already present.
    public void insert(K key, V value) {
        if (!has(key)) addNew(key, value);
    }

    // Update the value when the key is already present.
    public void put(K key, V value) {
        Entry<K, V> existing = find(key);
        if (existing == null) addNew(key, value);
        else existing.value = value;
    }

    public V get(K key) {
        Entry<K, V> entry = find(key);
        return entry == null ? null : entry.value;
    }

    public boolean has(K key) {
        return find(key) != null;
    }

    public void remove(K key) {
        // TODO: Unlink a single matching entry.
        throw new UnsupportedOperationException("Unlink a single matching entry");
    }

    private void resize() {
        // TODO: Rebuild each chain at the new capacity.
        throw new UnsupportedOperationException("Rebuild each chain at the new capacity");
    }

    @Override
    public String toString() {
        var text = new StringBuilder();
        for (var chain : buckets) {
            for (var entry : chain) {
                text.append(entry.key).append(": ").append(entry.value).append('\n');
            }
        }
        return text.toString();
    }
}
