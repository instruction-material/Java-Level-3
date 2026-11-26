import java.util.ArrayList;
public class BubbleSort implements Sort {
    public ArrayList<Integer> sort(ArrayList<Integer> numbers) {
        bubbleSort(numbers);
        return numbers;
    };
    public void bubbleSort(ArrayList<Integer> nums) {
        /*
    Because an element gets sorted every time we make a pass, we can repeat the inner loop less and less as we go on.
    The outer loop counts down from the second to last element so that the inner loop can count up to the outer loop’s counter.
    */
        for (int i = nums.size() - 1; i > 0; i--)
            //make one pass through the list
            for (int j = 0; j < i; j++)
                //swap the pair of elements...
                //if they're in the wrong order
                if (nums.get(j) > nums.get(j + 1)) {
                    int temp = nums.get(j);
                    nums.set(j, nums.get(j + 1));
                    nums.set(j + 1, temp);
                }
    }
}
