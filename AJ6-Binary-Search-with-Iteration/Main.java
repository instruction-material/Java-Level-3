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
        int result = binarySearch(arr, target);
        if (result == -1) {
            System.out.println(target + " not present");
        } else {
            System.out.println(target + " found at index " + result);
        }
    }

    public static int binarySearch(int[] numbers, int target) {
        int lower = 0;
        int upper = numbers.length - 1;

        while (lower <= upper) {

            int middle = (int)((lower + upper) / 2);

            if (target == numbers[middle]) {
                return middle;
            } else if (target > numbers[middle]) {
                lower = middle + 1;
            } else {
                upper = middle - 1;
            }
        }

        return -1;
    }
}
