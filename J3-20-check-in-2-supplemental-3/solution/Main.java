import java.util.Arrays;

public class Main {
    static int[] smallest(int[] input, int k) {
        if (k < 0 || k > input.length) throw new IllegalArgumentException("k is out of range.");
        if (k == 0) return new int[0];
        int[] values = input.clone();
        for (int start = 0; start < k; start++) {
            int minimum = start;
            for (int i = start + 1; i < values.length; i++) {
                if (values[i] < values[minimum]) minimum = i;
            }
            int saved = values[start];
            values[start] = values[minimum];
            values[minimum] = saved;
        }
        return Arrays.copyOf(values, k);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(smallest(new int[]{8, -3, 5, -3, 2}, 3)));
    }
}
