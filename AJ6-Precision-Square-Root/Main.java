import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in).useLocale(Locale.ROOT)) {
            System.out.print("\nEnter a nonnegative number: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Enter a finite nonnegative number.");
                return;
            }

            try {
                System.out.println("Square Root: " + sqrt(scanner.nextDouble()));
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            }
        }
    }

    public static double sqrt(double n) {
        if (!Double.isFinite(n) || n < 0.0) {
            throw new IllegalArgumentException("Enter a finite nonnegative number.");
        }
        if (n == 0.0) {
            return n;
        }

        double left = 0.0;
        // A fractional input can have a root larger than the input itself.
        double right = Math.max(1.0, n);
        while (Math.nextUp(left) < right) {
            double middle = left + (right - left) / 2.0;
            // This comparison expresses middle squared <= n without overflow.
            if (middle <= n / middle) {
                left = middle;
            } else {
                right = middle;
            }
        }

        return left;
    }
}
