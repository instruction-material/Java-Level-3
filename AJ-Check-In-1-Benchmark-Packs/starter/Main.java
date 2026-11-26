import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.function.IntPredicate;

class Main {
    private static final int ARR_SIZE = 100_000;
    private static final int ARR_RANGE = 1_000_000;
    private static final int QUERY_COUNT = 2_000;
    private static final int ROUNDS = 4;
    private static final Random random = new Random(2026L);

    record Trial(long elapsedNanos, int matches) {}

    public static void main(String[] args) {
        int[] array = createArray(ARR_SIZE, ARR_RANGE);
        Arrays.sort(array);
        int[] targets = new int[QUERY_COUNT];
        for (int i = 0; i < targets.length; i += 2) {
            targets[i] = array[random.nextInt(array.length)];
            targets[i + 1] = -1;
        }
        IntPredicate linearLookup = target -> linear(array, target);
        IntPredicate binaryLookup = target -> binary(array, 0, array.length - 1, target) != -1;

        // Warm both search methods using the same short set of queries.
        int[] warmup = Arrays.copyOf(targets, 200);
        measure(warmup, linearLookup);
        measure(warmup, binaryLookup);

        long linearNanos = 0;
        long binaryNanos = 0;
        for (int round = 0; round < ROUNDS; round++) {
            boolean linearFirst = round % 2 == 0;
            Trial first = measure(targets, linearFirst ? linearLookup : binaryLookup);
            Trial second = measure(targets, linearFirst ? binaryLookup : linearLookup);
            if (first.matches() != targets.length / 2 || second.matches() != first.matches()) {
                throw new IllegalStateException("Search results did not match the known workload.");
            }
            linearNanos += (linearFirst ? first : second).elapsedNanos();
            binaryNanos += (linearFirst ? second : first).elapsedNanos();
        }

        System.out.printf(Locale.ROOT, "%d queries per round, %d hits and %d misses, %d rounds%n",
                targets.length, targets.length / 2, targets.length / 2, ROUNDS);
        System.out.printf(Locale.ROOT, "Linear search mean: %.3f ms%n", meanMillis(linearNanos));
        System.out.printf(Locale.ROOT, "Binary search mean: %.3f ms%n", meanMillis(binaryNanos));
    }

    static Trial measure(int[] targets, IntPredicate lookup) {
        // TODO: Time and count the identical target list.
        throw new UnsupportedOperationException("Time and count the identical target list");
    }

    private static double meanMillis(long totalNanos) {
        return totalNanos / (ROUNDS * 1_000_000.0);
    }

    public static int[] createArray(int size, int range) {
        int[] values = new int[size];
        for (int index = 0; index < values.length; index++) {
            values[index] = random.nextInt(range);
        }
        return values;
    }

    public static boolean linear(int[] nums, int target) {
        for (int num : nums) {
            if (num == target) return true;
        }
        return false;
    }

    public static int binary(int[] numbers, int lower, int upper, int target) {
        if (upper < lower) return -1;

        int middle = lower + (upper - lower) / 2;
        if (target == numbers[middle]) return middle;
        if (target > numbers[middle]) {
            return binary(numbers, middle + 1, upper, target);
        }
        return binary(numbers, lower, middle - 1, target);
    }
}
