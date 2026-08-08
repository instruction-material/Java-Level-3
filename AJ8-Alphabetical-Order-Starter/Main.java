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
    }
}
