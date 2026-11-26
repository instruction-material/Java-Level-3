import java.util.SplittableRandom;

public class ContractTests {
    static void invalidIndex(ListContract<Integer> list, int index) {
        try {
            list.get(index);
            throw new AssertionError("Accepted index " + index);
        } catch (IndexOutOfBoundsException expected) {
            // Invalid indices are rejected by the list contract.
        }
    }
    static void expect(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        if (args.length == 1 && args[0].equals("find")) {
            list.insertBack(4);
            list.insertBack(null);
            list.insertBack(4);
            expect(0, list.firstIndex(4));
            expect(1, list.firstIndex(null));
            expect(-1, list.firstIndex(8));
            expect(-1, new LinkedList<Integer>().firstIndex(null));
            System.out.println("Find checks passed");
            return;
        }

        list.insertFront(4);
        list.clearAll();

        var expected = new java.util.ArrayList<Integer>();
        var random = new SplittableRandom(20261126L);
        for (int round = 0; round < 2048; round++) {
            Integer value = round % 7 == 0 ? null : random.nextInt(-5, 6);
            switch (random.nextInt(7)) {
                case 0, 1 -> { expected.addFirst(value); list.insertFront(value); }
                case 2, 3 -> { expected.addLast(value); list.insertBack(value); }
                case 4 -> { if (!expected.isEmpty()) expected.removeFirst(); list.removeFront(); }
                case 5 -> { if (!expected.isEmpty()) expected.removeLast(); list.removeBack(); }
                case 6 -> { expected.clear(); if (!list.clearAll()) throw new AssertionError("Clear failed"); }
            }
            expect(expected.size(), list.getLength());
            expect(expected.isEmpty(), list.isEmpty());
            expect(expected.toString(), list.toString());
            for (int i = 0; i < expected.size(); i++) {
                if (!java.util.Objects.equals(expected.get(i), list.get(i))) throw new AssertionError("Wrong index");
            }
            invalidIndex(list, -1);
            invalidIndex(list, expected.size());
        }
        list.clearAll();
        for (int value = 0; value < 8192; value++) list.insertFront(value);
        if (!list.clearAll() || !list.clearAll() || !list.isEmpty()) throw new AssertionError("Clear did not reset");
        list.insertBack(9);
        expect(9, list.get(0));
        System.out.println("List checks passed");
    }
}
