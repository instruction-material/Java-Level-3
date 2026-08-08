public class Deque {
    private final int MAX;
    private int deque[];
    private int size;
    private int nextFront; //index for inserting the next front item
    private int nextBack;  //index for inserting the next back item

    /* When we insert an item, we’ll go to the insert index ("nextFront" or "nextBack"), insert the item, and then update the insert index so that it’s ready for the next time we insert. These variables will also keep track of the beginning and end of the deque.
  */

    public Deque(int maxSize) {
        MAX = maxSize;
        deque = new int[MAX];
        nextFront = convertToIndex(0);
        nextBack = convertToIndex(1);
        this.size = 0;
    }

    // Converts a number into a valid index in the circular array
    private int convertToIndex(int number) {
        number = number % MAX;
        if (number < 0) {
            number = number + MAX;
        }
        return number;
    }

    // Checks whether Deque is full or not.
    public boolean isFull() {
        return size == MAX;
    }

    // Checks whether Deque is empty or not.
    public boolean isEmpty() {
        return size == 0;
    }

    // Inserts an item at the front
    public void insertFront(int item) {

        if (isFull()) {
            System.out.println("Overflow");
            throw new OverflowException();
        } else {
            deque[nextFront] = item;
            nextFront = convertToIndex(nextFront - 1);
            size++;
        }
    }

    // Inserts an item at the nextBack
    public void insertBack(int item) {
        if (isFull()) {
            System.out.println("Overflow");
            throw new OverflowException();
        } else {
            deque[nextBack] = item;
            nextBack = convertToIndex(nextBack + 1);
            size++;
        }
    }

    // Deletes element at front end of Deque
    public void removeFront() {
        if (isEmpty()) {
            System.out.println("Underflow");
            throw new UnderflowException();
        } else {
            nextFront = convertToIndex(nextFront + 1);
            size--;
        }
    }

    // Delete element at back end of Deque
    public void removeBack() {
        if (isEmpty()) {
            System.out.println("Underflow");
            throw new UnderflowException();
        } else {
            nextBack = convertToIndex(nextBack - 1);
            size--;
        }
    }

    // Returns front element of Deque
    public int getFront() {
        if (isEmpty()) {
            System.out.println("Underflow");
            throw new UnderflowException();
        }
        int index = convertToIndex(nextFront + 1);
        return deque[index];
    }

    // function return back element of Deque
    public int getBack() {
        if (isEmpty()) {
            System.out.println("Underflow");
            throw new UnderflowException();
        }
        int index = convertToIndex(nextBack - 1);
        return deque[index];
    }

    @Override
    public String toString() {
        String result = "";
        int start = nextFront + 1;
        for (int i = 0; i < size; i++) {
            int index = convertToIndex(start + i);
            result += deque[index] + " ";
        }
        return result;
    }
}
