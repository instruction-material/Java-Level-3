import java.util.ArrayList;
import java.util.List;

public class ContractTests {
    public static void main(String[] args) {
        List<Integer> visited = new ArrayList<>();
        Main.Trial trial = Main.measure(new int[]{8, 3, 9, 1}, target -> {
            visited.add(target);
            return target % 2 == 1;
        });
        if (!visited.equals(List.of(8, 3, 9, 1)) || trial.matches() != 3
                || trial.elapsedNanos() < 0) {
            throw new AssertionError("Expected one query per target, in order, with three hits");
        }
        if (Main.measure(new int[0], target -> {
            throw new AssertionError("Empty input has no queries");
        }).matches() != 0) {
            throw new AssertionError("Empty workload should yield zero hits");
        }
        int[] values = {1, 6, 10, 20};
        for (int target = -1; target <= 21; target++) {
            boolean binary = Main.binary(values, 0, values.length - 1, target) >= 0;
            if (binary != Main.linear(values, target)) throw new AssertionError(target);
        }
        System.out.println("Benchmark checks passed");
    }
}
