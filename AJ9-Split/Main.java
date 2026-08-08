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
        System.out.println("\n" + nums);
        split(nums);
    }

    public static void split(ArrayList<Integer> nums) {

        //base case
        if (nums.size() <= 1) {
            System.out.println(nums);
            return;
        }

        //recursive case
        int midpoint = nums.size() / 2;

        split(new ArrayList<>(nums.subList(0, midpoint)));
        split(new ArrayList<>(nums.subList(midpoint, nums.size())));
    }
}
