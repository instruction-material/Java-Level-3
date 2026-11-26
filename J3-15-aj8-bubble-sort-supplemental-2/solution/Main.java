import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static List<int[]> snapshots(int[] input) {
        int[] values = input.clone();
        List<int[]> output = new ArrayList<>();
        for (int end = values.length - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    int saved = values[i];
                    values[i] = values[i + 1];
                    values[i + 1] = saved;
                }
            }
            output.add(values.clone());
        }
        return output;
    }

    public static void main(String[] args) {
        for (int[] row : snapshots(new int[]{4, 2, 3, 1})) {
            System.out.println(Arrays.toString(row));
        }
    }
}
