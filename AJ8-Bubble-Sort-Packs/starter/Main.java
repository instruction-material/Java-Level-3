import java.util.Arrays;

public class Main {
    static long sort(int[] values, boolean earlyStop) {
        // TODO: Sort adjacent values with an optional no-swap stop.
        throw new UnsupportedOperationException("Sort adjacent values with an optional no-swap stop");
    }

    static long lastExchange(int[] values) {
        // TODO: Stop at the last exchange in the previous pass.
        throw new UnsupportedOperationException("Stop at the last exchange in the previous pass");
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
