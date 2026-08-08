import java.util.*;

class Main {
    public static void main(String[] args) {
        int[] arr = {2,  4,  10, 27, 38,  39,  40, 41,
                     42, 65, 72, 95, 100, 133, 142};

        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a target: ");
        int target = scanner.nextInt();
        int result = binarySearch(arr, 0, arr.length - 1, target);
        if (result == -1) {
            System.out.println(target + " not present");
        } else {
            System.out.println(target + " found at index " + result);
        }
    }

    /*
  In-Place Implementation:
  This implementation keeps track of the lower and upper bounds rather than literally spliting the array in half
  Parameters: the numbers, lower bound, upper bound, and the target
  Returns: the index of the element if it's found, -1 if it's not
  */
    public static int binarySearch(int[] numbers, int lower, int upper,
                                   int target) {

        //base case
        if (upper < lower) {
            return -1;
        }

        //recursive case
        int middle = (int)((upper + lower) / 2);
        if (target == numbers[middle]) {
            //we found the number!
            return middle;

        } else if (target > numbers[middle]) {
            //target is greater than the middle number
            lower = middle + 1;
            return binarySearch(numbers, lower, upper, target);

        } else {
            //target is less than the middle number
            upper = middle - 1;
            return binarySearch(numbers, lower, upper, target);
        }
    }

    /*
  Out-of-Place Implementation:
  This implementation literally splits the array in half.
  Parameters: the numbers and the target
  Returns: true if it found the target, false if it didn't

  When you literally split the list in half, it's more difficult to determine the index of the target, so that's why it returns True or False rather than the index.
  */
    public static boolean binarySearch(int[] numbers, int target) {

        //base case
        if (numbers.length == 0) {
            return false;
        }

        //recursive case
        int middle = (int)(numbers.length / 2);
        if (target == numbers[middle]) {

            //we found the number!
            return true;

        } else if (target > numbers[middle]) {

            //target is greater than the middle number
            int[] half =
                Arrays.copyOfRange(numbers, middle + 1, numbers.length);
            return binarySearch(half, target);

        } else {

            //target is less than the middle number
            int[] half = Arrays.copyOfRange(numbers, 0, middle);
            return binarySearch(half, target);
        }
    }
}
