import java.util.ArrayList;
import java.util.Random;

class Main {
    public static void main(String[] args) {
        int size = 10;
        ArrayList<Integer> nums = new ArrayList<>();
        Random random = new Random();

        //add random numbers
        for (int i = 0; i < size; i++) {
            nums.add(random.nextInt(100));
        }

        //print, sort, and print again
        System.out.println();
        System.out.println(nums);
        nums = mergeSort(nums);
        System.out.println(nums);
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

        //compare the first elements of the two lists,
        //move the smaller of the two to the "result" list
        //repeat until one of the lists is empty
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

        //one list will run out of elements before the other,
        //so add the rest of the remaining elements
        if (a.size() == 0) {
            result.addAll(b);
        }
        if (b.size() == 0) {
            result.addAll(a);
        }

        return result;
    }
}
