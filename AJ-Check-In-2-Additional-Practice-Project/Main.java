import java.util.*;

class Main {

    private static final int LIST_RANGE = 1000;
    private static final int LIST_SIZE = 10000;
    private static final int NUM_TESTS = 1;

    // 10000 1
    // 100 10000

    // NOTES:
    // insertion sort is best on small lists (<1000)
    // merge sort is best on large lists (>1000)
    // "LIST_RANGE" is the range for picking random numbers
    // increasing "NUM_TESTS" amplifies differences (but takes longer)

    public static void main(String[] args) {

        System.out.println();
        System.out.println(LIST_SIZE + " elements");
        System.out.println(NUM_TESTS + " tests\n");

        System.out.print("Selection Sort: ");
        timeTest(new SelectionSort());
        System.out.print("Insertion Sort: ");
        timeTest(new InsertionSort());
        System.out.print("   Bubble Sort: ");
        timeTest(new BubbleSort());
        System.out.print("    Merge Sort: ");
        timeTest(new MergeSort());
    }

    private static double timeTest(Sort algorithm) {

        // begin tests
        ArrayList<Integer> list;
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUM_TESTS; i++) {
            list = ListGenerator.getRandom(LIST_SIZE, LIST_RANGE);
            algorithm.sort(list);
        }
        long end = System.currentTimeMillis();

        // display results
        double time = (end - start) / 1000.0;
        System.out.println(time + " seconds!");
        return time;
    }
}
