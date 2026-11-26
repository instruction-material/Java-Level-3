import java.util.Set;

public final class ContractTests {
    private ContractTests() {}

    public static void main(String[] args) {
        Set<String> formats = Set.of("1", "log(n)", "n", "n log(n)", "n^2", "log(n) + m^2");
        if (Main.rows() != 10) throw new AssertionError("Keep all ten worksheet rows.");
        for (int row = 0; row < Main.rows(); row++) {
            if (Main.expression(row).isBlank() || !formats.contains(Main.growth(row))) {
                throw new AssertionError("Complete row " + (row + 1) + " with one of " + formats);
            }
        }
        System.out.println("All rows use an accepted label format. Check the reasoning separately.");
    }
}
