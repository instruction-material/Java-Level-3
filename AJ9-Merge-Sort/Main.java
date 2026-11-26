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
            return new ArrayList<>(nums);
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
}
