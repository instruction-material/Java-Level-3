import java.util.Arrays;

public class Main {
    static int[] union(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        int j = 0;
        int written = 0;
        while (i < left.length || j < right.length) {
            int next;
            if (j == right.length || (i < left.length && left[i] <= right[j])) next = left[i++];
            else next = right[j++];
            if (written == 0 || result[written - 1] != next) result[written++] = next;
        }
        return Arrays.copyOf(result, written);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(union(new int[]{1, 1, 4, 8}, new int[]{1, 2, 8, 9})));
    }
}
