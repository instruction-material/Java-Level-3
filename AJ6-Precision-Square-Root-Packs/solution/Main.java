public class Main {
    static double approximate(double number) {
        if (!Double.isFinite(number) || number < 0.0) {
            throw new IllegalArgumentException("Expected a finite nonnegative number.");
        }
        if (number == 0.0) return number;

        double lower = 0.0;
        double upper = Math.max(1.0, number);
        while (Math.nextUp(lower) < upper) {
            double middle = lower + (upper - lower) / 2.0;
            if (middle <= number / middle) lower = middle;
            else upper = middle;
        }
        return lower;
    }

    public static void main(String[] args) {
        double number = args.length == 0 ? 0.25 : Double.parseDouble(args[0]);
        System.out.println(approximate(number));
    }
}
