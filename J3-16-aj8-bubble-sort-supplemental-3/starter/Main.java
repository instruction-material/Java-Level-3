import java.util.Arrays;

public class Main {
    static void sort(int[] values) {
        // TODO: Move the largest and smallest items toward opposite ends.
        throw new UnsupportedOperationException("Move the largest and smallest items toward opposite ends");
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
