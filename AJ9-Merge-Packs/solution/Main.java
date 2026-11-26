import java.util.Arrays;

public class Main {
    record Entry(int key, String label) {}

    static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        int j = 0;
        int write = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) result[write++] = left[i++];
            else result[write++] = right[j++];
        }
        while (i < left.length) result[write++] = left[i++];
        while (j < right.length) result[write++] = right[j++];
        return result;
    }

    static Entry[] mergeEntries(Entry[] left, Entry[] right) {
        Entry[] result = new Entry[left.length + right.length];
        int i = 0;
        int j = 0;
        int write = 0;
        while (i < left.length && j < right.length) {
            if (left[i].key() <= right[j].key()) result[write++] = left[i++];
            else result[write++] = right[j++];
        }
        while (i < left.length) result[write++] = left[i++];
        while (j < right.length) result[write++] = right[j++];
        return result;
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
