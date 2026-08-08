import java.util.*;
public class BinarySearch {
    public static void main(String[] args) {

        ///////////////////////////////
        // BINARY SEARCH
        ///////////////////////////////

        /*

    Binary-1: What is Binary Search? When can we use it? What are the two ways we can implement it? Can you describe how it works?

    Binary-2: The binarySearch() method takes in a list and sorts the numbers from largest to smallest, but it's incomplete. Fill in the missing code to create two working Binary Search methods, one that works iteratively and one that works recursively. Be sure to test the method after adding your code!
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

        while (lower <= upper) {
            int middle = (int)((lower + upper) / 2);

            // YOUR CODE HERE
        }
        return -1;
    }

    public static int binarySearchRecur(int[] numbers, int target, int lower,
                                        int upper) {

        //YOUR CODE HERE

        int middle = (int)((upper + lower) / 2);

        if (target == numbers[middle]) {
            return middle;
        } else if (target > numbers[middle]) {
            //YOUR CODE HERE
        } else {
            //YOUR CODE HERE
        }
        return -1;
    }
}
