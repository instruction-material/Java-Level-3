import java.util.*;
public class MergeSort {
    public static void main(String[] args) {

        /////////////////////////////
        // MERGE SORT
        /////////////////////////////

        //Merge-1: What is Merge Sort? Can you describe how it works?
        //Merge-1: Merge the two sorted sublists. Explain the process that you followed.
        int[] list4 = {1, 4, 7, 10};
        int[] list5 = {2, 3, 5, 11};
        //ANSWER: {1,2,3,4,5,7,10,11}

        //Merge-2: Finish the incomplete mergeSort() method. You can use the merge() helper method that has been provided. Be sure to test the method after adding your code!
        ArrayList<Integer> nums = generateList(10);
        mergeSort(nums);

        //Merge-3: What is the time complexity of Merge Sort? Can you describe the best and worst case scenarios?
        //ANSWER: O(nlog(n))
        //All the scenarios are roughly the same

        //Merge-3: How does the time-complexity of Merge Sort compare to the other sorting algorithms?

        //ANSWER: Merge sort is significantly faster. It works much better on large datasets. For small datasets, insertion sort is the best choice because the recursion in merge sort introduces a lot of overhead.
    }

    public static ArrayList<Integer> mergeSort(ArrayList<Integer> nums) {
        //base case
        if (nums.size() <= 1) {
            return nums;
        }

        //recursive case
        int midpoint = nums.size() / 2;

        //sort the left and right halves with mergeSort
        ArrayList<Integer> leftHalf =
            mergeSort(new ArrayList<>(nums.subList(0, midpoint)));
        ArrayList<Integer> rightHalf =
            mergeSort(new ArrayList<>(nums.subList(midpoint, nums.size())));

        return merge(leftHalf, rightHalf);
    }

    public static ArrayList<Integer> merge(ArrayList<Integer> a,
                                           ArrayList<Integer> b) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        while (a.size() != 0 && b.size() != 0) {
            if (a.get(0) < b.get(0)) {
                result.add(a.get(0));
                a.remove(0);
            } else {
                result.add(b.get(0));
                b.remove(0);
            }
        }
        if (a.size() == 0) {
            result.addAll(b);
        }
        if (b.size() == 0) {
            result.addAll(a);
        }
        return result;
    }

    public static ArrayList<Integer> generateList(int size) {
        ArrayList<Integer> nums = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            nums.add(random.nextInt(100));
        }
        return nums;
    }
}
