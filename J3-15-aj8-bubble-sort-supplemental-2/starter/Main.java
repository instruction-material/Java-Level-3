import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static List<int[]> snapshots(int[] input) {
        // TODO: Capture the array at the end of each shrinking pass.
        throw new UnsupportedOperationException("Capture the array at the end of each shrinking pass");
    }

    public static void main(String[] args) {
        for (int[] row : snapshots(new int[]{4, 2, 3, 1})) {
            System.out.println(Arrays.toString(row));
        }
    }
}
