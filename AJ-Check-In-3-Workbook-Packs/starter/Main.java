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
        // TODO: Read lines until an integer or end of input.
        throw new UnsupportedOperationException("Read lines until an integer or end of input");
    }

    static <T> int indexOf(Node<T> first, T query) {
        // TODO: Find the first equal data value in the chain.
        throw new UnsupportedOperationException("Find the first equal data value in the chain");
    }

    static <T> boolean removeFirst(DoubleChain<T> chain, T query) {
        // TODO: Unlink the first equal nonsentinel node.
        throw new UnsupportedOperationException("Unlink the first equal nonsentinel node");
    }

    static String applyBackspaces(String presses) {
        // TODO: Use a stack to remove the latest typed code point.
        throw new UnsupportedOperationException("Use a stack to remove the latest typed code point");
    }

    static List<String> rotate(List<String> names, int rotations) {
        // TODO: Move the queue head to the back for each rotation.
        throw new UnsupportedOperationException("Move the queue head to the back for each rotation");
    }

    static List<Integer> priorityOrder(List<Integer> values) {
        // TODO: Poll the smallest available value until the queue is empty.
        throw new UnsupportedOperationException("Poll the smallest available value until the queue is empty");
    }

    static Map<String, Integer> pair(List<String> keys, List<Integer> values) {
        // TODO: Build a map using the latest value for each key.
        throw new UnsupportedOperationException("Build a map using the latest value for each key");
    }

    static Map<String, List<Integer>> group(List<String> keys, List<Integer> values) {
        // TODO: Group all values for a repeated key.
        throw new UnsupportedOperationException("Group all values for a repeated key");
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
