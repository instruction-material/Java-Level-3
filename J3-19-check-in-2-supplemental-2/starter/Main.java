import java.util.HashMap;

public class Main {
    static boolean valid(int[] input, int[] candidate) {
        // TODO: Verify both order and the multiset of values.
        throw new UnsupportedOperationException("Verify both order and the multiset of values");
    }

    public static void main(String[] args) {
        int[] input = {7, -3, 7, 1};
        System.out.println(valid(input, new int[]{-3, 1, 7, 7}));
        System.out.println(valid(input, new int[]{-3, 1, 1, 7}));
    }
}
