public class Main {
    static int insertionIndex(int[] values, int target) {
        int lower = 0;
        int upper = values.length;
        while (lower < upper) {
            int middle = lower + (upper - lower) / 2;
            if (values[middle] < target) lower = middle + 1;
            else upper = middle;
        }
        return lower;
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 4, 4, 9};
        System.out.println(insertionIndex(sample, 4));
        System.out.println(insertionIndex(sample, 5));
        System.out.println(insertionIndex(sample, 10));
    }
}
