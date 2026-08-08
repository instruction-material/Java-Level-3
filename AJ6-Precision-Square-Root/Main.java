import java.util.*;
class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a number: ");
        int num = scanner.nextInt();

        System.out.print("Square Root: ");
        System.out.println(sqrt(num));
    }

    public static double sqrt(double n) {

        double left = 0;  //start left at the smallest
        double right = n; //start right at the biggest

        while (right - left > 1e-6) {

            //calculate the new middle
            double middle = (left + right) / 2;

            if (middle * middle <= n) {
                //too small, move the left up
                left = middle;
            } else {
                //too big, move the right down
                right = middle;
            }
        }

        return left;
    }
}
