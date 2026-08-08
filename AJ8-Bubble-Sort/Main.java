import java.util.Random;

class Main {
    public static void main(String[] args) {

        int size = 10;
        int[] nums = new int[size];
        Random random = new Random();

        //add random numbers
        for (int i = 0; i < size; i++) {
            nums[i] = random.nextInt(100);
        }

        //print, sort, and print again
        System.out.println();
        printArray(nums);
        bubbleSort(nums);
        printArray(nums);
    }

    public static void bubbleSort(int[] nums) {
        /*
    Because an element gets sorted every time we make a pass, we can repeat the inner loop less and less as we go on.
    The outer loop counts down from the second to last element so that the inner loop can count up to the outer loop’s counter.
    */
        for (int i = nums.length - 1; i > 0; i--)
            //make one pass through the list
            for (int j = 0; j < i; j++)
                //swap the pair of elements...
                //if they're in the wrong order
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
