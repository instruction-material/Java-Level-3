public class BubbleSort {
    public static void main(String[] args) {

        /////////////////////////////
        // BUBBLE SORT
        /////////////////////////////

        //Bubble-1: What is Bubble Sort? Can you describe how it works?

        //Bubble-1: What does list3 look like after 2 passes of Bubble Sort?
        int[] list3 = {50, 23, 12, 43, 12, 20};
        //ANSWER: {12, 23, 12, 20, 43, 50}

        //Bubble-2: Finish the incomplete bubbleSort() method. Be sure to test the method after adding your code!
        bubbleSort(list3);

        //Bubble-3: What is the time complexity of Bubble Sort? Can you describe the best and worst case scenarios?
        //ANSWER: O(n^2)
        //Best-Case/Worst-Case: There is none because it's always the same (but you could optimize it to realize when it's done)

        //Bubble-3: How does Bubble Sort compare to Insertion Sort and Selection Sort?
        //ANSWER: It's generally slower.
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
}
