import java.util.*;
public class LinearSearch {
    public static void main(String[] args) {

        ///////////////////////////////
        // LINEAR SEARCH
        ///////////////////////////////

        /*
    Linear-1: What is Linear Search? When is it useful? Can you describe how it works?

    Linear-2: The linearSearch() method takes in a list and searches for a particular number, but it's incomplete. Fill in the missing code to create a working linear search function. Be sure to test the method after adding your code!
    */

        int[] nums = new int[100];
        for (int i = 0; i < 100; i++) {
            nums[i] = (int)(Math.random() * 100);
        }
        System.out.println(Arrays.toString(nums));

        /*
    Linear-3: What is the time complexity of Linear Search? Can you describe the best and worst case scenarios?
    */
    }
    public static boolean linearSearch(int[] array, int target) {
        for (int num : array) {
            // YOUR CODE HERE
        }
        return false;
    }
}
