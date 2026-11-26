public class Main {
    static double approximate(double number) {
        // TODO: Bisect the interval containing the square root.
        throw new UnsupportedOperationException("Bisect the interval containing the square root");
    }

    public static void main(String[] args) {
        double number = args.length == 0 ? 0.25 : Double.parseDouble(args[0]);
        System.out.println(approximate(number));
    }
}
