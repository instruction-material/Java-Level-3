import java.util.ArrayList;
public class InsertionSort implements Sort {
    public ArrayList<Integer> sort(ArrayList<Integer> numbers) {
        insertionSort(numbers);
        return numbers;
    };

    public void insertionSort(ArrayList<Integer> nums) {
        for (int i = 1; i < nums.size(); i++) {
            //get the item to insert
            int itemToInsert = nums.get(i);
            int insertIndex = i;

            //shift the items right until it's ready to insert
            while (insertIndex > 0 &&
                   itemToInsert < nums.get(insertIndex - 1)) {
                nums.set(insertIndex, nums.get(insertIndex - 1));
                insertIndex--;
            }
            //insert the item
            nums.set(insertIndex, itemToInsert);
        }
    }
}
