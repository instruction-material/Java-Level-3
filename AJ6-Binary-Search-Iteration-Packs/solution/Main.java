public class Main {
    record SearchResult(int index, int probes) {}

    static int search(int[] values, int target) {
        int lower = 0;
        int upper = values.length - 1;
        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            if (values[middle] == target) return middle;
            if (values[middle] < target) lower = middle + 1;
            else upper = middle - 1;
        }
        return -1;
    }

    static SearchResult measure(int[] values, int target) {
        int lower = 0;
        int upper = values.length - 1;
        int probes = 0;
        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            probes++;
            if (values[middle] == target) return new SearchResult(middle, probes);
            if (values[middle] < target) lower = middle + 1;
            else upper = middle - 1;
        }
        return new SearchResult(-1, probes);
    }

    public static void main(String[] args) {
        int[] sample = {2, 4, 10, 27, 38};
        if (args.length == 1 && args[0].equals("count")) {
            System.out.println(measure(sample, 2));
            System.out.println(measure(sample, 28));
            return;
        }
        System.out.println(search(sample, 2));
        System.out.println(search(sample, 28));
    }
}
