public class Main {
    static String trace(int[] values, int target) {
        var steps = new java.util.StringJoiner(" ");
        int lower = 0;
        int upper = values.length - 1;
        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            steps.add(lower + ":" + middle + ":" + upper);
            if (values[middle] == target) break;
            if (values[middle] < target) lower = middle + 1;
            else upper = middle - 1;
        }
        return steps.toString();
    }

    public static void main(String[] args) {
        System.out.println(trace(new int[]{2, 4, 10, 27, 38}, 28));
    }
}
