import java.util.*;

class Main {

    private static final int ARR_SIZE = 100000;
    private static final int ARR_RANGE = 1000000;
    private static final int NUM_TESTS = 100000;
    private static final Random random = new Random();

    public static void main(String[] args) {

        int[] array = createArray(ARR_SIZE, ARR_RANGE);
        System.out.println(NUM_TESTS + " tests");

        // linear search
        System.out.println("\nStarting Linear Search...");
        long linearStart = System.currentTimeMillis();
        for (int i = 0; i < NUM_TESTS; i++) {
            linear(array, random.nextInt(ARR_RANGE));
        }
        long linearEnd = System.currentTimeMillis();
        double linearTime = (linearEnd - linearStart) / 1000.0;
        System.out.println("Completed in " + linearTime + " seconds!");

        Arrays.sort(array);

        // binary search
        System.out.println("\nStarting Binary Search...");
        long binaryStart = System.currentTimeMillis();
        for (int i = 0; i < NUM_TESTS; i++) {
            binary(array, 0, array.length - 1, random.nextInt(ARR_RANGE));
        }
        long binaryEnd = System.currentTimeMillis();
        double binaryTime = (binaryEnd - binaryStart) / 1000.0;
        System.out.println("Completed in " + binaryTime + " seconds!");
    }

    public static int[] createArray(int size, int range) {
        System.out.println("\nCreating an array with:");
        System.out.println(ARR_SIZE + " elements");
        System.out.println(ARR_RANGE + " possible numbers");
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = random.nextInt(range);
        }
        return nums;
    }

    public static boolean linear(int[] nums, int target) {
        for (int num : nums) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

    public static int binary(int[] numbers, int lower, int upper, int target) {

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
            return binary(numbers, lower, upper, target);

        } else {
            //target is less than the middle number
            upper = middle - 1;
            return binary(numbers, lower, upper, target);
        }
    }
}
