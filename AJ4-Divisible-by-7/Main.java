import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nEnter a number: ");
            int number = scanner.nextInt();

            if (isDivBySeven(number)) {
                System.out.println(number + " is divisible by 7");
            } else {
                System.out.println(number + " is NOT divisible by 7");
            }
        }
    }

    public static boolean isDivBySeven(int n) {

        //base cases
        if (n == 0 || n == 7) {
            return true;
        }
        if (n < 10) {
            return false;
        }

        //recursive case
        int rightMost = n % 10;
        int everythingElse = (int)(n / 10);

        int result = everythingElse - (2 * rightMost);
        result = Math.abs(result);

        return isDivBySeven(result);
    }
}
