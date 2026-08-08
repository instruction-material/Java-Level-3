import java.util.*;
public class LinearSearch {
    public static void main(String[] args) {

        ///////////////////////////////
        // LINEAR SEARCH
        ///////////////////////////////

        /*
    Linear-1: What is Linear Search? When is it useful? Can you describe how it works?

    ANSWER: Linear search searches a list for a target number. It works on any type of list, sorted or unsorted. It works by examining each item one at a time, starting from the front and moving to the back.

    Linear-2: The linearSearch() method takes in a list and searches for a particular number, but it's incomplete. Fill in the missing code to create a working linear search function. Be sure to test the method after adding your code!
    */

        int[] nums = new int[100];
        for (int i = 0; i < 100; i++) {
            nums[i] = (int)(Math.random() * 100);
        }

        System.out.println(Arrays.toString(nums));
        System.out.println(linearSearch(nums, 27));

        /*
    Linear-3: What is the time complexity of Linear Search? Can you describe the best and worst case scenarios?

    ANSWER: O(n) linear-time
    Best-Case: O(1), the item is at the very front
    Worst-Case: O(n), the item is not in the array
    */

        //
    }
    public static boolean linearSearch(int[] array, int target) {
        for (int num : array) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }
}
