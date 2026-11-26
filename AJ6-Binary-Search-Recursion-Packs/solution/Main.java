public class Main {
    static int search(int[] values, int target) {
        return searchRange(values, target, 0, values.length - 1);
    }

    static int searchRange(int[] values, int target, int lower, int upper) {
        if (lower > upper) return -1;
        int middle = lower + (upper - lower) / 2;
        if (values[middle] == target) return middle;
        if (values[middle] < target) {
            return searchRange(values, target, middle + 1, upper);
        }
        return searchRange(values, target, lower, middle - 1);
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        System.out.println(search(sample, 38));
        System.out.println(search(sample, 39));
    }
}
