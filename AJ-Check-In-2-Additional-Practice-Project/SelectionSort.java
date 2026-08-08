import java.util.ArrayList;
public class SelectionSort implements Sort {

    public ArrayList<Integer> sort(ArrayList<Integer> numbers) {
        return selectionSort(numbers);
    };

    public ArrayList<Integer> selectionSort(ArrayList<Integer> unsortedList) {
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
