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
        insertionSort(nums);
        printArray(nums);
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

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
