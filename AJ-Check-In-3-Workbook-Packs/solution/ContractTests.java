import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        try (var input = new java.util.Scanner("not a number\n2147483648\n -42 \n")) {
            expect(java.util.OptionalInt.of(-42), Main.firstInteger(input));
            expect(java.util.OptionalInt.empty(), Main.firstInteger(input));
        }
        var first = new Main.Node<String>("oak");
        first.next = new Main.Node<>(null);
        first.next.next = new Main.Node<>("oak");
        expect(0, Main.indexOf(first, new String("oak")));
        expect(1, Main.indexOf(first, null));
        expect(-1, Main.indexOf(first, "elm"));
        expect(-1, Main.indexOf(null, null));
        var chain = new Main.DoubleChain<String>();
        expect(false, Main.removeFirst(chain, null));
        chain.append(null);
        chain.append("elm");
        chain.append(null);
        expect(true, Main.removeFirst(chain, null));
        expect(java.util.Arrays.asList("elm", null), chain.values());
        expect(true, Main.removeFirst(chain, null));
        expect(List.of("elm"), chain.values());
        expect(false, Main.removeFirst(chain, null));
        expect(1, chain.size);
        expect("elm", chain.tail.previous.value);
        expect(true, Main.removeFirst(chain, "elm"));
        expect(0, chain.size);
        expect(chain.head, chain.tail.previous);
        expect(chain.tail, chain.head.next);
        expect("", Main.applyBackspaces("##"));
        expect("acdc", Main.applyBackspaces("ab#cdef##c"));
        expect("a𐐨", Main.applyBackspaces("a😀#𐐨"));
        expect(List.of("blue", "red", "green"), Main.rotate(List.of("red", "green", "blue"), 5));
        expect(List.of(), Main.rotate(List.of(), 2));
        try {
            Main.rotate(List.of("red"), -1);
            throw new AssertionError("Negative rotations");
        } catch (IllegalArgumentException expected) {
            // Negative counts are outside the operation contract.
        }
        expect(List.of(-1, 2, 2, 5), Main.priorityOrder(List.of(2, -1, 5, 2)));
        expect(List.of(), Main.priorityOrder(List.of()));
        var names = List.of("oak", "elm", "oak");
        var counts = List.of(2, 3, 5);
        expect(Map.of("oak", 5, "elm", 3), Main.pair(names, counts));
        try {
            Main.pair(names, List.of(2));
            throw new AssertionError("Mismatched input lengths");
        } catch (IllegalArgumentException expected) {
            // The result requires a value for every key.
        }
        if (args.length == 1 && args[0].equals("groups")) {
            expect(Map.of("oak", List.of(2, 5), "elm", List.of(3)), Main.group(names, counts));
            expect(List.of("oak", "elm"), List.copyOf(Main.group(names, counts).keySet()));
            expect(Map.of(), Main.group(List.of(), List.of()));
            System.out.println("Grouped collections checks passed");
            return;
        }

        System.out.println("Collections checkpoint checks passed");
    }
}
