import java.util.Arrays;

public class Main {
    static int[] sort(int[] values) {
        if (values.length <= 1) return values.clone();
        int middle = values.length / 2;
        int[] left = sort(Arrays.copyOfRange(values, 0, middle));
        int[] right = sort(Arrays.copyOfRange(values, middle, values.length));
        return merge(left, right);
    }

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

    public static void main(String[] args) {
        int[] values = {4, -3, 8, 4, 2};
        System.out.println(Arrays.toString(sort(values)));
    }
}
