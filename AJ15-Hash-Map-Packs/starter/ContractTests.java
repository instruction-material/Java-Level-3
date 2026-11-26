import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    private record Key(String id, int hash) {
        @Override public int hashCode() { return hash; }
    }

    static void absent(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected rejection");
    }

    public static void main(String[] args) {
        var table = new HashMap<Key, Integer>();
        var first = new Key("first", 46);
        var second = new Key("second", 46);
        table.insert(first, 1);
        table.insert(second, 2);
        expect(1, table.remove(first));
        expect(2, table.get(second));
        absent(() -> table.insert(new Key("second", 46), 99));
        expect(1, table.size());
        table.put(new Key("second", 46), 20);
        expect(20, table.get(second));
        absent(() -> table.put(first, 99));

        var used = new HashMap<Integer, Integer>();
        for (int i = 0; i < 47; i++) {
            used.insert(i, i);
            expect(i, used.remove(i));
        }
        expect(false, used.has(-1));
        absent(() -> used.get(999));
        used.insert(Integer.MIN_VALUE, 9);
        expect(9, used.get(Integer.MIN_VALUE));

        var model = new java.util.HashMap<Key, Integer>();
        var tested = new HashMap<Key, Integer>();
        var random = new java.util.SplittableRandom(15);
        for (int operation = 0; operation < 1200; operation++) {
            int id = random.nextInt(100);
            var key = new Key("id" + id, id % 4 - 2);
            if (random.nextBoolean()) {
                if (model.containsKey(key)) tested.put(key, operation);
                else tested.insert(key, operation);
                model.put(key, operation);
            } else if (model.containsKey(key)) {
                expect(model.remove(key), tested.remove(key));
            }
            expect(model.size(), tested.size());
            for (var entry : model.entrySet()) {
                expect(entry.getValue(), tested.get(entry.getKey()));
            }
        }
        if (args.length == 1 && args[0].equals("compact")) {
            int before = used.capacity();
            expect(true, used.deletedSlots() > 0);
            used.compact();
            expect(0, used.deletedSlots());
            expect(before, used.capacity());
            expect(1, used.size());
            expect(9, used.get(Integer.MIN_VALUE));
            expect(false, used.has(5));
            System.out.println("Compaction checks passed");
            return;
        }

        System.out.println("Hash table checks passed");
    }
}
