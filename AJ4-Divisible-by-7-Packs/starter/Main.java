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
        throw new UnsupportedOperationException("Implement isDivBySeven");
    }
}
