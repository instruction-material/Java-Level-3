import java.util.*;

class Main {
    static long[] doubled(int[] input) {
        long[] result = new long[input.length];
        for (int i = 0; i < input.length; i++) result[i] = 2L * input[i];
        return result;
    }

    static long sumEvens(int[] input) {
        long sum = 0;
        for (int value : input) if (value % 2 == 0) sum += value;
        return sum;
    }
    static double[] randomValues(int size, Random random) {
        if (size < 0) throw new IllegalArgumentException("Negative sample size");
        double[] values = new double[size];
        for (int i = 0; i < size; i++) values[i] = random.nextDouble(15, 100);
        return values;
    }
    static long totalLengths(String[] words) {
        long total = 0;
        for (String word : words) total += word.length();
        return total;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(doubled(new int[]{3, -2, 5})));
        System.out.println(sumEvens(new int[]{3, -2, 4}));
        System.out.println(totalLengths(new String[]{"oak", "elm"}));
    }
}
