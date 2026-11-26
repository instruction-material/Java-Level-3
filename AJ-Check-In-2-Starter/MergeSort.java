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

        //Merge-2: Finish the incomplete mergeSort() method. You can use the merge() helper method that has been provided. Be sure to test the method after adding your code!
        ArrayList<Integer> nums = generateList(10);
        mergeSort(nums);

        //Merge-3: What is the time complexity of Merge Sort? Can you describe the best and worst case scenarios?

        //Merge-3: How does the time-complexity of Merge Sort compare to the other sorting algorithms?
    }

    public static ArrayList<Integer> mergeSort(ArrayList<Integer> nums) {

        //base case
        if (nums.size() <= 1) {
            return new ArrayList<>(nums);
        }
        // TODO: sort both smaller halves and merge the two returned lists.
        throw new UnsupportedOperationException("Complete the recursive merge sort");
    }

    public static ArrayList<Integer> merge(ArrayList<Integer> a,
                                           ArrayList<Integer> b) {
        // Move a cursor past each copied value and keep both input lists intact.
        ArrayList<Integer> result = new ArrayList<>(a.size() + b.size());
        int left = 0;
        int right = 0;
        while (left < a.size() && right < b.size()) {
            Integer fromLeft = a.get(left);
            Integer fromRight = b.get(right);
            if (fromLeft <= fromRight) {
                result.add(fromLeft);
                left++;
            } else {
                result.add(fromRight);
                right++;
            }
        }
        result.addAll(a.subList(left, a.size()));
        result.addAll(b.subList(right, b.size()));
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
