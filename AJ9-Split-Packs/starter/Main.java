import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<List<Integer>> leaves(int[] values) {
        List<List<Integer>> output = new ArrayList<>();
        split(values, 0, values.length, output);
        return output;
    }

    static void split(int[] values, int start, int end, List<List<Integer>> output) {
        // TODO: Divide each range until it gives singleton leaves.
        throw new UnsupportedOperationException("Divide each range until it gives singleton leaves");
    }

    public static void main(String[] args) {
        System.out.println(leaves(new int[]{-1, 8, 3, 8, 0}));
    }
}
