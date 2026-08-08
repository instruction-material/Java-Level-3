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
        nums = selectionSort(nums);
        System.out.println(nums);
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
