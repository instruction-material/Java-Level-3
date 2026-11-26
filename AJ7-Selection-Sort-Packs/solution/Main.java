import java.util.Arrays;

public class Main {
    static long sort(int[] values) {
        long comparisons = 0;
        for (int start = 0; start + 1 < values.length; start++) {
            int smallest = start;
            for (int candidate = start + 1; candidate < values.length; candidate++) {
                comparisons++;
                if (values[candidate] < values[smallest]) smallest = candidate;
            }
            if (smallest != start) {
                int saved = values[start];
                values[start] = values[smallest];
                values[smallest] = saved;
            }
        }
        return comparisons;
    }

    public static void main(String[] args) {
        int[] values = {4, 1, 3, 1};
        long comparisons = sort(values);
        System.out.println(Arrays.toString(values));
        System.out.println("Comparisons: " + comparisons);
    }
}
