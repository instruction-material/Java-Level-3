import java.util.Arrays;

public class Main {
    static long sort(int[] values, boolean earlyStop) {
        long comparisons = 0;
        for (int end = values.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int index = 0; index < end; index++) {
                comparisons++;
                if (values[index] > values[index + 1]) {
                    int saved = values[index];
                    values[index] = values[index + 1];
                    values[index + 1] = saved;
                    swapped = true;
                }
            }
            if (earlyStop && !swapped) break;
        }
        return comparisons;
    }

    static long lastExchange(int[] values) {
        long comparisons = 0;
        int end = values.length - 1;
        while (end > 0) {
            int last = 0;
            for (int index = 0; index < end; index++) {
                comparisons++;
                if (values[index] > values[index + 1]) {
                    int saved = values[index];
                    values[index] = values[index + 1];
                    values[index + 1] = saved;
                    last = index;
                }
            }
            end = last;
        }
        return comparisons;
    }

    public static void main(String[] args) {
        int[] values = {2, 1, 3, 4, 5};
        if (args.length == 1 && args[0].equals("last")) {
            System.out.println("Last-exchange comparisons: " + lastExchange(values));
        } else {
            System.out.println("Early-stop comparisons: " + sort(values, true));
        }
        System.out.println(Arrays.toString(values));
    }
}
