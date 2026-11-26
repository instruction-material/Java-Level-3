public class Main {
    record SearchResult(int index, int probes) {}

    static int search(int[] values, int target) {
        // TODO: Search the inclusive range.
        throw new UnsupportedOperationException("Search the inclusive range");
    }

    static SearchResult measure(int[] values, int target) {
        // TODO: Count the midpoint probes.
        throw new UnsupportedOperationException("Count the midpoint probes");
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
