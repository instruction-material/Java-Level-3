import java.util.Random;

public class ContractTests {
    public static void main(String[] args) {
        int checks = 0;
        for (int n = -100000; n <= 100000; n++) {
            check(n);
            checks++;
        }
        for (int n : new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE + 1,
                Integer.MAX_VALUE - 1, Integer.MAX_VALUE}) {
            check(n);
            checks++;
        }
        Random random = new Random(7127);
        for (int i = 0; i < 20000; i++) {
            check(random.nextInt());
            checks++;
        }
        System.out.println("Divisibility contracts: " + checks + " passed");
    }
    private static void check(int n) {
        boolean expected = n % 7 == 0;
        boolean actual = Main.isDivBySeven(n);
        if (actual != expected) {
            throw new AssertionError(n + ": expected " + expected + ", got " + actual);
        }
    }
}
