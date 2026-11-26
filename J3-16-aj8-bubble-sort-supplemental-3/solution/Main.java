import java.util.Arrays;

public class Main {
    static void sort(int[] values) {
        int lower = 0;
        int upper = values.length - 1;
        while (lower < upper) {
            boolean swapped = false;
            for (int i = lower; i < upper; i++) {
                if (values[i] > values[i + 1]) {
                    exchange(values, i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
            upper--;
            swapped = false;
            for (int i = upper; i > lower; i--) {
                if (values[i - 1] > values[i]) {
                    exchange(values, i - 1, i);
                    swapped = true;
                }
            }
            if (!swapped) break;
            lower++;
        }
    }

    private static void exchange(int[] values, int first, int second) {
        int saved = values[first];
        values[first] = values[second];
        values[second] = saved;
    }

    public static void main(String[] args) {
        int[] sample = {2, 3, 8, 4, -1, 2};
        sort(sample);
        System.out.println(Arrays.toString(sample));
    }
}
