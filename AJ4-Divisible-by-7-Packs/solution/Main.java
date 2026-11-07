import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNext()) {
                String token = input.next();
                if (token.equalsIgnoreCase("quit")) return;
                try {
                    int number = Integer.parseInt(token);
                    System.out.println(number + (isDivBySeven(number)
                            ? " is divisible by 7" : " is NOT divisible by 7"));
                } catch (NumberFormatException error) {
                    System.out.println("Invalid integer.");
                }
            }
        }
    }

    // All signed int inputs, including Integer.MIN_VALUE, are accepted.
    public static boolean isDivBySeven(int n) {
        long magnitude = Math.abs((long) n);
        if (magnitude < 10) return magnitude == 0 || magnitude == 7;
        long remaining = magnitude / 10;
        long lastDigit = magnitude % 10;
        // This reduced value fits int and its magnitude is strictly smaller.
        return isDivBySeven((int) (remaining - 2 * lastDigit));
    }
}
