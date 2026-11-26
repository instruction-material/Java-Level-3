import java.util.Arrays;

public class ContractTests {
    public static void main(String[] args) {
        for (int size = 0; size <= 32; size++) {
            for (int first = 0; first <= size; first++) {
                boolean[] trials = new boolean[size];
                Arrays.fill(trials, first, size, true);
                int expected = first == size ? -1 : first;
                if (Main.firstPassing(trials) != expected) {
                    throw new AssertionError("Wrong boundary for size " + size + " and first " + first);
                }
            }
        }
        System.out.println("Boundary checks passed");
    }
}
