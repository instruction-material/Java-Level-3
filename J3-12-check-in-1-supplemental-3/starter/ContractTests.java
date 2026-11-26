public class ContractTests {
    public static void main(String[] args) {
        for (int size = 0; size <= 32; size++) {
            long counted = 0;
            for (int outer = 0; outer < size; outer++) {
                for (int within = 0; within < size / 4; within++) {
                    for (int repeat = 0; repeat < 2; repeat++) counted++;
                }
            }
            if (Main.visits(size) != counted) throw new AssertionError(size);
        }
        if (Main.visits(Integer.MAX_VALUE) != 2305843003844984834L) {
            throw new AssertionError("Long arithmetic must cover large inputs");
        }
        try {
            Main.visits(-1);
            throw new AssertionError("Negative size");
        } catch (IllegalArgumentException expected) {
            // Negative sizes are outside the loop-size contract.
        }
        System.out.println("Loop-count checks passed");
    }
}
