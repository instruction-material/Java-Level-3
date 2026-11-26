import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

class Main {
    private static final int SIZE = 300;
    private static final int VALUE_RANGE = 1000;
    private static final int ROUNDS = 4;

    record Algorithm(String name, Sort implementation) {}

    public static void main(String[] args) {
        List<Algorithm> algorithms = List.of(
                new Algorithm("Selection sort", new SelectionSort()),
                new Algorithm("Insertion sort", new InsertionSort()),
                new Algorithm("Bubble sort", new BubbleSort()),
                new Algorithm("Merge sort", new MergeSort()));
        Random random = new Random(2026L);
        ArrayList<Integer> warmup = ListGenerator.getRandom(SIZE, VALUE_RANGE, random);
        for (Algorithm algorithm : algorithms) {
            timed(algorithm, warmup, ascending(warmup));
        }

        System.out.printf(Locale.ROOT, "%d values per trial, %d trials per input order%n", SIZE, ROUNDS);
        for (String order : List.of("random", "ascending", "descending")) {
            long[] elapsed = new long[algorithms.size()];
            for (int round = 0; round < ROUNDS; round++) {
                ArrayList<Integer> input = ListGenerator.getRandom(SIZE, VALUE_RANGE, random);
                if (order.equals("ascending")) input.sort(Comparator.naturalOrder());
                if (order.equals("descending")) input.sort(Comparator.reverseOrder());
                ArrayList<Integer> expected = ascending(input);
                for (int offset = 0; offset < algorithms.size(); offset++) {
                    // Each algorithm runs first once for every input order.
                    int index = (offset + round) % algorithms.size();
                    elapsed[index] += timed(algorithms.get(index), input, expected);
                }
            }
            System.out.println(order);
            for (int index = 0; index < algorithms.size(); index++) {
                double meanMillis = elapsed[index] / (ROUNDS * 1_000_000.0);
                System.out.printf(Locale.ROOT, "%s mean: %.3f ms%n", algorithms.get(index).name(), meanMillis);
            }
        }
    }

    private static ArrayList<Integer> ascending(ArrayList<Integer> input) {
        ArrayList<Integer> expected = new ArrayList<>(input);
        expected.sort(Comparator.naturalOrder());
        return expected;
    }

    static long timed(Algorithm algorithm, ArrayList<Integer> input, ArrayList<Integer> expected) {
        ArrayList<Integer> working = new ArrayList<>(input);
        long start = System.nanoTime();
        ArrayList<Integer> result = algorithm.implementation().sort(working);
        long elapsed = System.nanoTime() - start;
        if (!expected.equals(result)) {
            throw new IllegalStateException(algorithm.name() + " did not preserve and sort the same values.");
        }
        return elapsed;
    }
}
