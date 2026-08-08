import java.util.*;
public class InsertionSort {
    public static void main(String[] args) {

        /////////////////////////////
        // INSERTION SORT
        /////////////////////////////

        //Insertion-1: What is Insertion Sort? Can you describe how it works?

        //Insertion-1: What will list2 look like after 3 passes of Insertion Sort?
        int[] list2 = {99, 15, 27, 13, 8, 1};

        //Insertion-2: Finish the incomplete insertionSort() method. Be sure to test the method after adding your code!
        insertionSort(list2);

        //Insertion-3: What is the time complexity of Insertion Sort? Can you describe the best and worst case scenarios?

        //Insertion-4: How does insertion sort differ from selection sort? Which algorithm is generally faster? Why?
    }

    public static void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {

            //get the item to insert
            int itemToInsert = nums[i];
            int insertIndex = i;

            // YOUR CODE HERE

            //insert the item
            nums[insertIndex] = itemToInsert;
        }
    }
}
