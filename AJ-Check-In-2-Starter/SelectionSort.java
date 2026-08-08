import java.util.*;

public class SelectionSort {
    public static void main(String[] args) {

        /////////////////////////////
        // SELECTION SORT
        /////////////////////////////

        //Selection-1: What is Selection Sort? Can you describe how it works by drawing out an example?

        //Selection-2: What will list1 look like after 2 passes of Selection Sort?
        Integer[] list1 = {34, 23, 76, 75, 14, 25};

        //Selection-2: Fill in the missing code to create a working Selection Sort method. Be sure to test the method after adding your code!

        ArrayList<Integer> nums = new ArrayList<>();
        Collections.addAll(nums, list1);
        selectionSort(nums);

        // Selection-3: What is the time complexity of Selection Sort? Can you describe the best and worst case scenarios?

        //Selection-4: Is this implementation in-place or out-of-place? How could we convert it from one to the other?
    }
    public static ArrayList<Integer>
    selectionSort(ArrayList<Integer> unsortedList) {

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (unsortedList.size() > 0) {

            int indexOfSmallest = 0;

            // YOUR CODE HERE

            int smallest = unsortedList.get(indexOfSmallest);
            sortedList.add(smallest);
            unsortedList.remove(indexOfSmallest);
        }
        return sortedList;
    }
}
