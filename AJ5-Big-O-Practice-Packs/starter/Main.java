public final class Main {
    private static final String[] EXPRESSIONS = {
        "12n^2 + n", "n - sqrt(n)", "11000000n + 5", "1000000n - 1000000",
        "1234567 + 6 - 5", "log(n) + 2", "n + log(n)", "1 + 2 + ... + n",
        "3log2(n) + m^2", "f(n) = 1 + f(n/2), f(1) = 0"
    };
    private static final String[] LABELS = {
        "?", "?", "?", "?", "?", "?", "?", "?", "?", "?" // Fill the labels in worksheet order.
    };

    private Main() {}

    public static String expression(int row) { return EXPRESSIONS[row]; }
    public static String growth(int row) { return LABELS[row]; }
    public static int rows() { return EXPRESSIONS.length; }

    public static void main(String[] args) {
        for (int row = 0; row < rows(); row++) {
            System.out.printf("%d. %s -> %s%n", row + 1, expression(row), growth(row));
        }
    }
}
