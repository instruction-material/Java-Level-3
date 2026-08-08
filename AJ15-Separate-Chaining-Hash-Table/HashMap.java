//TODO solve it recursively!!!!
public class HashMap<K, V> {

    private int numNodes;
    private int tableSize;
    private Node[] hashTable;
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashMap() {
        numNodes = 0;
        tableSize = 47;
        hashTable = new Node[tableSize];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % tableSize);
    }
    private Node getNode(int index) {
        return (Node)hashTable[index];
    }

    public void insert(K key, V value) {
        int index = hash(key);
        System.out.println(index);
        Node current = hashTable[index];
        while (current != null) {
            if (current.key.equals(key)) {
                //ERROR! duplicate key!
                return;
            }
            current = current.next;
        }
        current = new Node(key, value);
        //System.out.println(current.key + " " + current.value);
    }

    public V get(K key) {
        int index = hash(key);
        Node current = getNode(index);
        while (current != null) {
            System.out.println("TEST");
            if (current.key.equals(key)) {
                return (V)current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        Node previous = null;
        Node current = getNode(index);

        while (current != null) {
            if (current.key.equals(key)) {
                //found what we need to delete
                //set the previous node's next node...
                //...to the current node's next node
                previous.next = current.next;
                current = null;
                return;
            }
            previous = current;
            current = current.next;
        }

        //key doesn't exist
        return;
    }

    public void put(K key, V value) {
        int index = hash(key);
        Node current = getNode(index);
        if (current == null) {
            hashTable[index] = new Node(key, value);
        } else {
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node(key, value);
        }
    }
    public boolean has(K key) {
        int index = hash(key);
        Node current = getNode(index);
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    private void rehash() {
        System.out.println("REHASH!!");
        int oldtableSize = this.tableSize;
        this.tableSize *= 2;

        int i = 0;
        while (this.tableSize > this.PRIMES[i]) {
            i++;
        }
        this.tableSize = this.PRIMES[i];
        System.out.println(tableSize);

        //Create a new array with the bigger size
        Node[] bigger = new Node[this.tableSize];

        //For each element in the old array
        for (int j = 0; j < oldtableSize; j++) {

            Node node = getNode(j);

            //If the node exists insert the entire linked list
            //...into the bigger table
            if (node != null) {
                rehashInsert(node, bigger);
            }
        }
        this.hashTable = bigger; // Overwrite the hash table
    }

    private void rehashInsert(Node node, Node[] bigger) {
        int hashCode = hash((K)node.key);
        Node current = (Node)bigger[hashCode];
        while (current != null) {
            current = current.next;
        }
        current = node;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hashTable.length; i++) {
            Node current = getNode(i);
            while (current != null) {
                current = current.next;
                stringBuilder.append("\n" + current.key + ": ");
                stringBuilder.append(current.value);
            }
        }
        return stringBuilder.toString();
    }
}
