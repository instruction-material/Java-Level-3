import java.util.*;

public class SelectionSort {
    public static void main(String[] args) {

        /////////////////////////////
        // SELECTION SORT
        /////////////////////////////

        //Selection-1: What is Selection Sort? Can you describe how it works by drawing out an example?

        //Selection-2: What will list1 look like after 2 passes of Selection Sort?
        Integer[] list1 = {34, 23, 76, 75, 14, 25};
        //ANSWER: {14, 23} {34, 76, 75, 25}

        //Selection-2: Fill in the missing code to create a working Selection Sort method. Be sure to test the method after adding your code!

        ArrayList<Integer> nums = new ArrayList<>();
        Collections.addAll(nums, list1);
        selectionSort(nums);

        // Selection-3: What is the time complexity of Selection Sort? Can you describe the best and worst case scenarios?
        //ANSWER: O(n^2), all the scenarios are the same

        //Selection-4: Is this implementation in-place or out-of-place? How could we convert it from one to the other?

        //ANSWER: Out-of-Place. To make it in-place, we could divide the list into a sorted portion on the left and an unsorted portion on the right. For each pass we could move the next smallest item to the end of the sorted portion instead of to a seperate list.
    }
    public static ArrayList<Integer>
    selectionSort(ArrayList<Integer> unsortedList) {
        ArrayList<Integer> sortedList = new ArrayList<>();
        while (unsortedList.size() > 0) {
            //get the next smallest
            int indexOfSmallest = 0;
            for (int i = 1; i < unsortedList.size(); i++) {
                if (unsortedList.get(i) < unsortedList.get(indexOfSmallest)) {
                    indexOfSmallest = i;
                }
            }

            //move the smallest to the sorted ArrayList
            int smallest = unsortedList.get(indexOfSmallest);
            sortedList.add(smallest);
            unsortedList.remove(indexOfSmallest);
        }
        return sortedList;
    }
}
