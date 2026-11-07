public class ContractTests {
    public static void main(String[] args) {
        for (int n = 0; n <= 100; n++) {
            if (Main.sum(n) != n * (n + 1) / 2) {
                throw new AssertionError("sum " + n);
            }
        }
        for (int n : new int[]{-1, 101, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            try {
                Main.sum(n);
            } catch (IllegalArgumentException expected) {
                continue;
            }
            throw new AssertionError("Accepted invalid input " + n);
        }
        System.out.println("Call-trace contracts: 105 passed");
    }
}
