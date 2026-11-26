import java.util.Arrays;

public final class Main {
    private Main() {}

    public static int firstIndex(int[] values, int target) {
        requireArray(values);
        for (int i = 0; i < values.length; i++) {
            if (values[i] == target) return i;
        }
        return -1;
    }

    public static int[] allIndices(int[] values, int target) {
        requireArray(values);
        int[] matches = new int[values.length];
        int count = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == target) matches[count++] = i;
        }
        return Arrays.copyOf(matches, count);
    }

    private static void requireArray(int[] values) {
        if (values == null) throw new IllegalArgumentException("values must not be null");
    }

    public static void main(String[] args) {
        int[] sample = {7, 2, 7, 4};
        if (args.length == 1 && args[0].equals("all")) {
            System.out.println(Arrays.toString(allIndices(sample, 7)));
            return;
        }
        int index = firstIndex(sample, 7);
        System.out.println(index >= 0 ? "7 found at index " + index : "7 is not in the array");
    }
}
