import java.util.*;
public class BinarySearch {
    public static void main(String[] args) {

        ///////////////////////////////
        // BINARY SEARCH
        ///////////////////////////////

        /*

    Binary-1: What is Binary Search? When can we use it? What are the two ways we can implement it? Can you describe how it works?

    Binary-2: The two methods search an ascending int array and return a matching index, or -1 when the target is absent. Trace an iterative and a recursive search and test a first-position hit, an absent target, and an empty array.
    */

        int[] nums = new int[100];
        int num = 0;
        for (int i = 0; i < 100; i++) {
            num += (int)(Math.random() * 10);
            nums[i] = num;
        }
        System.out.println(Arrays.toString(nums));

        /*
    Binary-3: What is the time complexity of Binary Search? Can you describe the best and worst case scenarios?

    */
    }
    public static int binarySearchIter(int[] numbers, int target) {
        int lower = 0;
        int upper = numbers.length - 1;

        if (lower > upper) return -1;

        // TODO: compare the midpoint and move a bound until the range is empty.
        throw new UnsupportedOperationException("Complete the iterative search");
    }

    public static int binarySearchRecur(int[] numbers, int target, int lower,
                                        int upper) {
        if (lower > upper) return -1;

        // TODO: compare the midpoint and return a call on a smaller range when needed.
        throw new UnsupportedOperationException("Complete the recursive search");
    }
}
