import java.util.Arrays;

public class Main {
    static int[] selection(int[] input, int passes) {
        // TODO: Trace the next minimum exchanges.
        throw new UnsupportedOperationException("Trace the next minimum exchanges");
    }

    static int[] insertion(int[] input, int passes) {
        // TODO: Trace the first insertions into the sorted prefix.
        throw new UnsupportedOperationException("Trace the first insertions into the sorted prefix");
    }

    static int[] bubble(int[] input, int passes) {
        // TODO: Trace the shrinking adjacent-swap passes.
        throw new UnsupportedOperationException("Trace the shrinking adjacent-swap passes");
    }

    static int[] sortMerge(int[] input) {
        // TODO: Sort by splitting and merging smaller results.
        throw new UnsupportedOperationException("Sort by splitting and merging smaller results");
    }

    static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        int j = 0;
        int write = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) result[write++] = left[i++];
            else result[write++] = right[j++];
        }
        while (i < left.length) result[write++] = left[i++];
        while (j < right.length) result[write++] = right[j++];
        return result;
    }

    static int[] rangeAfterSorting(int[] input, int target) {
        // TODO: Find the full run by searching both sorted boundaries.
        throw new UnsupportedOperationException("Find the full run by searching both sorted boundaries");
    }

    private static int boundary(int[] sorted, int target, boolean pastTies) {
        int lower = 0;
        int upper = sorted.length;
        while (lower < upper) {
            int middle = lower + (upper - lower) / 2;
            int item = sorted[middle];
            if (item < target || (pastTies && item == target)) lower = middle + 1;
            else upper = middle;
        }
        return lower;
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("range")) {
            System.out.println(Arrays.toString(rangeAfterSorting(new int[]{8, 2, 5, 2, 2}, 2)));
            return;
        }
        System.out.println(Arrays.toString(selection(new int[]{34, 23, 76, 75, 14, 25}, 2)));
        System.out.println(Arrays.toString(insertion(new int[]{99, 15, 27, 13, 8, 1}, 3)));
        System.out.println(Arrays.toString(bubble(new int[]{50, 23, 12, 43, 12, 20}, 2)));
        System.out.println(Arrays.toString(sortMerge(new int[]{7, 1, 3, 5, 11, 4, 10, 2})));
    }
}
