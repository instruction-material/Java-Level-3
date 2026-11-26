import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    record Riddle(String question, String answer) {}
    record Triple<A, B, C>(A first, B second, C third) {}

    static final class Node<T> {
        final T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    static final class DoubleNode<T> {
        final T value;
        DoubleNode<T> previous;
        DoubleNode<T> next;

        DoubleNode(T value) {
            this.value = value;
        }
    }

    static final class DoubleChain<T> {
        final DoubleNode<T> head = new DoubleNode<>(null);
        final DoubleNode<T> tail = new DoubleNode<>(null);
        int size;

        DoubleChain() {
            connect(head, tail);
        }

        void append(T value) {
            var node = new DoubleNode<>(value);
            connect(tail.previous, node);
            connect(node, tail);
            size++;
        }

        void connect(DoubleNode<T> before, DoubleNode<T> after) {
            before.next = after;
            after.previous = before;
        }

        List<T> values() {
            var result = new ArrayList<T>();
            for (var node = head.next; node != tail; node = node.next) result.add(node.value);
            return result;
        }
    }

    static OptionalInt firstInteger(Scanner input) {
        while (input.hasNextLine()) {
            String line = input.nextLine().strip();
            try {
                return OptionalInt.of(Integer.parseInt(line));
            } catch (NumberFormatException invalidLine) {
                // The entire invalid line was consumed.
            }
        }
        return OptionalInt.empty();
    }

    static <T> int indexOf(Node<T> first, T query) {
        int index = 0;
        for (var node = first; node != null; node = node.next) {
            if (Objects.equals(node.value, query)) return index;
            index++;
        }
        return -1;
    }

    static <T> boolean removeFirst(DoubleChain<T> chain, T query) {
        for (var node = chain.head.next; node != chain.tail; node = node.next) {
            if (Objects.equals(node.value, query)) {
                chain.connect(node.previous, node.next);
                chain.size--;
                return true;
            }
        }
        return false;
    }

    static String applyBackspaces(String presses) {
        var stack = new ArrayDeque<Integer>();
        presses.codePoints().forEachOrdered(key -> {
            if (key == '#') stack.pollLast();
            else stack.addLast(key);
        });
        var result = new StringBuilder();
        for (int key : stack) result.appendCodePoint(key);
        return result.toString();
    }

    static List<String> rotate(List<String> names, int rotations) {
        if (rotations < 0) throw new IllegalArgumentException("Negative rotation count");
        if (names.isEmpty()) return List.of();
        var queue = new ArrayDeque<>(names);
        for (int step = rotations % queue.size(); step > 0; step--) {
            queue.addLast(queue.removeFirst());
        }
        return List.copyOf(queue);
    }

    static List<Integer> priorityOrder(List<Integer> values) {
        var queue = new PriorityQueue<>(values);
        var result = new ArrayList<Integer>(values.size());
        while (!queue.isEmpty()) result.add(queue.remove());
        return result;
    }

    static Map<String, Integer> pair(List<String> keys, List<Integer> values) {
        if (keys.size() != values.size()) throw new IllegalArgumentException("Different list lengths");
        var entries = new LinkedHashMap<String, Integer>();
        var count = values.iterator();
        for (String key : keys) entries.put(key, count.next());
        return entries;
    }

    static Map<String, List<Integer>> group(List<String> keys, List<Integer> values) {
        if (keys.size() != values.size()) throw new IllegalArgumentException("Different list lengths");
        var result = new LinkedHashMap<String, List<Integer>>();
        var count = values.iterator();
        for (String key : keys) {
            result.computeIfAbsent(key, ignored -> new ArrayList<>()).add(count.next());
        }
        return result;
    }

    public static void main(String[] args) {
        var keys = List.of("oak", "elm", "oak");
        var counts = List.of(2, 3, 5);
        if (args.length == 1 && args[0].equals("groups")) {
            System.out.println(group(keys, counts));
            return;
        }
        var riddle = new Riddle("What can hold three values of different types?", "A triple");
        var triple = new Triple<>("oak", true, 2);
        System.out.println(riddle.answer() + ": " + triple);
        try (var input = new Scanner("bad\n 42 \n")) {
            var number = firstInteger(input);
            System.out.println(number.isPresent() ? number.getAsInt() : "No integer");
        }
        var first = new Node<>("oak");
        first.next = new Node<>("elm");
        System.out.println(indexOf(first, "elm"));
        var chain = new DoubleChain<String>();
        chain.append("oak");
        chain.append("elm");
        removeFirst(chain, "oak");
        System.out.println(chain.values());
        System.out.println(applyBackspaces("ab#cdef##c"));
        System.out.println(rotate(List.of("red", "green", "blue"), 2));
        System.out.println(priorityOrder(List.of(5, 2, 5, 1)));
        System.out.println(pair(keys, counts));
    }
}
