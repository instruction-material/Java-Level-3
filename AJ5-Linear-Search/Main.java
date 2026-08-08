import java.util.*;

class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};

        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a target: ");
        int target = scanner.nextInt();

        int index = linearSearch(arr, target);
        if (index > 0) {
            System.out.println(target + " found at index " + index);
        } else {
            System.out.println(target + " is not in the array!");
        }
    }

    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
