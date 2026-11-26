import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<List<Integer>> leaves(int[] values) {
        List<List<Integer>> output = new ArrayList<>();
        split(values, 0, values.length, output);
        return output;
    }

    static void split(int[] values, int start, int end, List<List<Integer>> output) {
        if (start == end) return;
        if (end - start == 1) {
            output.add(List.of(values[start]));
            return;
        }
        int middle = start + (end - start) / 2;
        split(values, start, middle, output);
        split(values, middle, end, output);
    }

    public static void main(String[] args) {
        System.out.println(leaves(new int[]{-1, 8, 3, 8, 0}));
    }
}
