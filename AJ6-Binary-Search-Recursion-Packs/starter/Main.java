public class Main {
    static int search(int[] values, int target) {
        return searchRange(values, target, 0, values.length - 1);
    }

    static int searchRange(int[] values, int target, int lower, int upper) {
        // TODO: Search a smaller range recursively.
        throw new UnsupportedOperationException("Search a smaller range recursively");
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        System.out.println(search(sample, 38));
        System.out.println(search(sample, 39));
    }
}
