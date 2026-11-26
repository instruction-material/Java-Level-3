import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.StringJoiner;

public class HashMap<K extends Comparable<K>, V> implements Map<K, V> {

    private int numNodes;
    private int tableSize;
    private ArrayList<Node> hashTable;

    private static final int[] PRIMES = {
        2,         3,        5,         7,         11,        13,
        17,        19,       23,        29,        31,        37,
        41,        43,       47,        53,        97,        193,
        389,       769,      1543,      3079,      6151,      12289,
        24593,     49157,    98317,     139291,    196613,    259537,
        299993,    350213,   393241,    444863,    499979,    550279,
        599999,    652153,   700001,    786433,    851359,    900001,
        950473,    999983,   1572869,   3145739,   6291469,   12582917,
        25165843,  50331653, 100663319, 201326611, 402653189, 805306457,
        1610612741};

    private class Node {
        private K key;
        private V value;
        private boolean tombStone;
        public Node(K k, V v) {
            this.key = k;
            this.value = v;
            this.tombStone = false;
        }
        @Override
        public String toString() {
            return this.key + ": " + this.value;
        }
    }

    //CONSTRUCTOR -----------------------------------------------
    public HashMap() {
        this.tableSize = 47;
        this.hashTable = new ArrayList<>(Collections.nCopies(tableSize, null));
    }

    private int getIndex(K key, boolean inserting) {
        Objects.requireNonNull(key, "key");
        int index = Math.floorMod(key.hashCode(), tableSize);
        int firstDeleted = -1;

        for (int probes = 0; probes < tableSize; probes++) {
            Node node = hashTable.get(index);
            if (node == null) {
                return inserting && firstDeleted >= 0 ? firstDeleted : index;
            }

            if (node.tombStone) {
                if (inserting && firstDeleted < 0) firstDeleted = index;
            } else if (key.equals(node.key)) {
                return index;
            }
            index = (index + 1) % tableSize;
        }
        return firstDeleted;
    }

    private Node getExistingNode(K key) {
        int index = getIndex(key, false);
        Node node = index < 0 ? null : hashTable.get(index);
        if (node == null) throw new IllegalArgumentException("Cannot find key");
        return node;
    }

    @Override
    public void insert(K key, V value) {
        int index = getIndex(key, true);
        Node existing = index < 0 ? null : hashTable.get(index);
        if (existing != null && !existing.tombStone) {
            throw new IllegalArgumentException("Key already exists");
        }
        if (index < 0 || 2L * (numNodes + 1) > tableSize) {
            rehash();
            index = getIndex(key, true);
        }
        hashTable.set(index, new Node(key, value));
        numNodes++;
    }

    @Override
    public V remove(K key) {
        Node node = getExistingNode(key);
        node.tombStone = true;
        numNodes--;
        return node.value;
    }

    @Override
    public void put(K key, V value) {
        getExistingNode(key).value = value;
    }

    @Override
    public V get(K key) {
        return getExistingNode(key).value;
    }

    @Override
    public boolean has(K key) {
        int index = getIndex(key, false);
        return index >= 0 && hashTable.get(index) != null;
    }

    @Override
    public int size() {
        return numNodes;
    }

    private void rehash() {
        long target = Math.min(2L * tableSize, PRIMES[PRIMES.length - 1]);
        int next = 0;
        while (PRIMES[next] < target) next++;
        if (PRIMES[next] <= tableSize) {
            throw new IllegalStateException("No larger supported table");
        }

        var oldTable = hashTable;
        tableSize = PRIMES[next];
        hashTable = new ArrayList<>(Collections.nCopies(tableSize, null));
        numNodes = 0;
        for (Node node : oldTable) {
            if (node != null && !node.tombStone) insert(node.key, node.value);
        }
    }

    @Override
    public String toString() {
        var entries = new StringJoiner(", ", "{", "}");
        for (Node node : hashTable) {
            if (node != null && !node.tombStone) entries.add(node.toString());
        }
        return entries.toString();
    }
}
