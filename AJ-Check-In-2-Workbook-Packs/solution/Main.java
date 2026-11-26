import java.util.Arrays;

public class Main {
    static int[] selection(int[] input, int passes) {
        int[] values = input.clone();
        for (int start = 0; start < passes && start + 1 < values.length; start++) {
            int smallest = start;
            for (int choice = start + 1; choice < values.length; choice++) {
                if (values[choice] < values[smallest]) smallest = choice;
            }
            int saved = values[start];
            values[start] = values[smallest];
            values[smallest] = saved;
        }
        return values;
    }

    static int[] insertion(int[] input, int passes) {
        int[] values = input.clone();
        for (int index = 1; index < values.length && index <= passes; index++) {
            int saved = values[index];
            int hole = index;
            while (hole > 0 && values[hole - 1] > saved) {
                values[hole] = values[hole - 1];
                hole--;
            }
            values[hole] = saved;
        }
        return values;
    }

    static int[] bubble(int[] input, int passes) {
        int[] values = input.clone();
        int end = values.length - 1;
        for (int pass = 0; pass < passes && end > 0; pass++, end--) {
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    int saved = values[i];
                    values[i] = values[i + 1];
                    values[i + 1] = saved;
                }
            }
        }
        return values;
    }

    static int[] sortMerge(int[] input) {
        if (input.length < 2) return input.clone();
        int middle = input.length / 2;
        return merge(sortMerge(Arrays.copyOfRange(input, 0, middle)),
                     sortMerge(Arrays.copyOfRange(input, middle, input.length)));
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
        int[] sorted = sortMerge(input);
        int first = boundary(sorted, target, false);
        if (first == sorted.length || sorted[first] != target) return new int[]{-1, -1};
        return new int[]{first, boundary(sorted, target, true)};
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
        if (args.length == 1 && args[0].equals("bubble")) {
            System.out.println(Arrays.toString(bubble(new int[]{50, 23, 12, 43, 12, 20}, 2)));
            return;
        }
        System.out.println(Arrays.toString(selection(new int[]{34, 23, 76, 75, 14, 25}, 2)));
        System.out.println(Arrays.toString(insertion(new int[]{99, 15, 27, 13, 8, 1}, 3)));
        System.out.println(Arrays.toString(sortMerge(new int[]{7, 1, 3, 5, 11, 4, 10, 2})));
    }
}
