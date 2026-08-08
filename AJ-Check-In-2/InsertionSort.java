import java.util.*;
public class InsertionSort {
    public static void main(String[] args) {

        /////////////////////////////
        // INSERTION SORT
        /////////////////////////////

        //Insertion-1: What is Insertion Sort? Can you describe how it works?

        //Insertion-1: What will list2 look like after 3 passes of Insertion Sort?
        int[] list2 = {99, 15, 27, 13, 8, 1};
        //ANSWER: {15, 27, 99, 13, 8, 1}

        //Insertion-2: Finish the incomplete insertionSort() method. Be sure to test the method after adding your code!
        insertionSort(list2);

        //Insertion-3: What is the time complexity of Insertion Sort? Can you describe the best and worst case scenarios?
        //ANSWER: O(n^2)
        //Best-Case: O(n), the list is already sorted
        //Worst-Case: O(n^2), the list is sorted in reverse order

        //Insertion-4: How does insertion sort differ from selection sort? Which algorithm is generally faster? Why?

        //ANSWER: Insertion sort grabs the next element and inserts it into the correct location, regardless of whether it is the biggest or the smallest. Insertion sort is generally faster because if an element is already in the correct location then we can just move on. In the best case scenario (an already sorted list) this requires O(n) time, whereas selection sort still takes the same amount of time: O(n^2)
    }

    public static void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {

            //get the item to insert
            int itemToInsert = nums[i];
            int insertIndex = i;

            //shift the items right until it's ready to insert
            while (insertIndex > 0 && itemToInsert < nums[insertIndex - 1]) {
                nums[insertIndex] = nums[insertIndex - 1];
                insertIndex--;
            }
            //insert the item
            nums[insertIndex] = itemToInsert;
        }
    }
}
