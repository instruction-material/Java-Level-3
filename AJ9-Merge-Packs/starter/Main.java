import java.util.Arrays;

public class Main {
    record Entry(int key, String label) {}

    static int[] merge(int[] left, int[] right) {
        // TODO: Merge both sorted arrays without changing them.
        throw new UnsupportedOperationException("Merge both sorted arrays without changing them");
    }

    static Entry[] mergeEntries(Entry[] left, Entry[] right) {
        // TODO: Merge records by key and retain their order on ties.
        throw new UnsupportedOperationException("Merge records by key and retain their order on ties");
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("stable")) {
            Entry[] entries = mergeEntries(new Entry[]{new Entry(4, "left")},
                    new Entry[]{new Entry(4, "right")});
            System.out.println(Arrays.stream(entries).map(Entry::label).toList());
            return;
        }
        System.out.println(Arrays.toString(merge(new int[]{-2, 1, 5}, new int[]{1, 2, 7, 9})));
    }
}
