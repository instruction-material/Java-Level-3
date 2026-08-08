public class BubbleSort {
    public static void main(String[] args) {

        /////////////////////////////
        // BUBBLE SORT
        /////////////////////////////

        //Bubble-1: What is Bubble Sort? Can you describe how it works?

        //Bubble-1: What does list3 look like after 2 passes of Bubble Sort?
        int[] list3 = {50, 23, 12, 43, 12, 20};

        //Bubble-2: Finish the incomplete bubbleSort() method. Be sure to test the method after adding your code!
        bubbleSort(list3);

        //Bubble-3: What is the time complexity of Bubble Sort? Can you describe the best and worst case scenarios?

        //Bubble-3: How does Bubble Sort compare to Insertion Sort and Selection Sort?
    }

    public static void bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length; i--)
            for (int j = 0; j < nums.length - 1; j++) {
                if (nums[j] > nums[j + 1]) {

                    // YOUR CODE HERE
                }
            }
    }
}
