public class Deque {
    private final int MAX;
    private int deque[];
    private int nextFront; //index for inserting the next front item
    private int nextBack;  //index for inserting the next back item
    private int size;

    public Deque(int maxSize) {
        MAX = maxSize;
        deque = new int[MAX];
        nextFront = convertToIndex(0);
        nextBack = convertToIndex(1);
        this.size = 0;
    }

    //Converts a number into a valid index in the circular array
    private int convertToIndex(int number) {
        /*
    Examples:
      Valid indicies for an array of size 5:
      0, 1, 2, 3, 4

      Given 5, return 0
      Given 3, return 3
      Given 1, return 1
      Given -1, return 4
      Given -3, return 2
    */
        //HINT: Use the mod operator (%) to wrap around to the beginning if we go over the end. If the initial number is negative, add the length of the array to wrap around to the end.
        return -1;
    }

    // Checks whether Deque is full or not.
    public boolean isFull() {
        return true;
    }

    // Checks whether Deque is empty or not.
    public boolean isEmpty() {
        return true;
    }

    // Inserts an item at the front
    public void insertFront(int item) {
    }

    // Inserts an item at the back
    public void insertBack(int item) {
    }

    // Deletes element at front end of Deque
    public void removeFront() {
    }

    // Delete element at back end of Deque
    public void removeBack() {
    }

    // Returns front element of Deque
    public int getFront() {
        return -1;
    }

    // Returns the last element in the Deque
    public int getBack() {
        return -1;
    }

    @Override
    public String toString() {
        return "";
    }
}
