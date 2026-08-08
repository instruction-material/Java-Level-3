import java.util.*;
import java.io.*;
class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("words.txt");
        Scanner scanner = new Scanner(file);
        ArrayList<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        System.out.println("\nBEFORE:\n" + words);
        bubbleSort(words);
        System.out.println("\nAFTER:\n" + words);
    }
    private static void bubbleSort(ArrayList<String> list) {
        for (int i = list.size() - 1; i > 0; i--)
            //make one pass through the list
            for (int j = 0; j < i; j++) {

                String first = list.get(j);
                String second = list.get(j + 1);

                //swap the pair of elements...
                //if they're in the wrong order
                if (first.compareTo(second) > 0) {
                    String temp = first;
                    list.set(j, second);
                    list.set(j + 1, temp);
                }
            }
    }
}
