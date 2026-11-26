import java.util.*;

class Main {
    static OptionalInt count(Scanner input) {
        while (input.hasNextLine()) {
            try {
                int value = Integer.parseInt(input.nextLine().strip());
                if (value >= 0) return OptionalInt.of(value);
            } catch (NumberFormatException ignored) {
                // Read the next complete line.
            }
        }
        return OptionalInt.empty();
    }
    public static void main(String[] args) {
        System.out.println(count(new Scanner("bad\n-3\n 7 \n8")).orElseThrow());
    }
}
