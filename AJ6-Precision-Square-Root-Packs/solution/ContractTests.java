public class ContractTests {
    public static void main(String[] args) {
        for (double number : new double[]{
                0, 0.25, 1, 2, 81, Double.MIN_VALUE, Double.MAX_VALUE}) {
            double expected = Math.sqrt(number);
            double result = Main.approximate(number);
            if (!Double.isFinite(result) || Math.abs(expected - result) > 2 * Math.ulp(expected)) {
                throw new AssertionError(number + " produced " + result + ", expected " + expected);
            }
        }
        if (Double.doubleToLongBits(Main.approximate(-0.0)) != Double.doubleToLongBits(-0.0)) {
            throw new AssertionError("Expected negative zero");
        }
        for (double invalid : new double[]{-1, Double.NaN, Double.POSITIVE_INFINITY}) {
            try {
                Main.approximate(invalid);
                throw new AssertionError("Accepted invalid input " + invalid);
            } catch (IllegalArgumentException expected) {
                // Invalid real-valued inputs are rejected.
            }
        }
        System.out.println("Square-root checks passed");
    }
}
