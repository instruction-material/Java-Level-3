import java.util.Scanner;
import java.util.ArrayList;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("\nHello! Please enter the number of books you own: ");
        int numberOfBooks = in.nextInt();

        ArrayList<Integer> books = new ArrayList<Integer>();
        for (int i = 0; i < numberOfBooks; i++) {
            System.out.print("Enter the number of pages in a book: ");
            books.add(in.nextInt());
        }

        System.out.print("\nOriginal: " + books);
        books = biggestToSmallest(books);
        System.out.print("\nSorted: " + books);
    }

    public static ArrayList<Integer>
    biggestToSmallest(ArrayList<Integer> unsortedList) {
        ArrayList<Integer> sortedList = new ArrayList<>();

        while (unsortedList.size() > 0) {

            //get the biggest
            int indexOfBiggest = 0;
            for (int i = 1; i < unsortedList.size(); i++) {
                if (unsortedList.get(i) > unsortedList.get(indexOfBiggest)) {
                    indexOfBiggest = i;
                }
            }

            //move the biggest to the sorted ArrayList
            int biggest = unsortedList.get(indexOfBiggest);
            sortedList.add(biggest);
            unsortedList.remove(indexOfBiggest);
        }
        return sortedList;
    }
}
