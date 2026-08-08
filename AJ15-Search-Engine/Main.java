import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/*Simplified search engine that takes in keywords from standard input and finds the corresponding url(s). The search results are then printed to standard output.*/

public final class Main {

    //Create a hashmap
    private static HashMap<String, ArrayList<String>> data = new HashMap<>();

    public static void main(String[] args) {

        //Prepare the search engine
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\nWelcome to the Juni Search Engine!");
            System.out.print("Please enter a file name: ");

            String fileName = scanner.next();
            File inputFile = new File(fileName);
            Scanner fileScanner = new Scanner(inputFile);

            loadFile(fileScanner);

        } catch (ArrayIndexOutOfBoundsException e) {
            //If the input file doesn't have the correct format
            System.err.println("ERROR: enter a valid input file name.");
            return;
        } catch (FileNotFoundException e) {
            //If the file is not found
            System.err.println("ERROR: file not found.");
            return;
        }

        //Run the search engine forever
        while (true) {
            System.out.print("\nSearch: ");
            performSearch(scanner.next());
        }
    }

    // Populates the HashMap
    private static void loadFile(Scanner fileScanner) {

        String url = null;
        String line = null;
        int numUrls = 0;

        System.out.println("Loading...");
        while (fileScanner.hasNextLine()) {

            line = fileScanner.nextLine();
            //If the line contains a space, there's a keyword
            if (line.contains(" ")) {
                String[] words = line.split(" ");
                addUrl(url, words);
                numUrls++;
            } else {
                //If the line doesn't contain a space, it's a url
                url = line;
            }
            //System.out.println(line);
        }

        //Print that it worked successfully
        System.out.println("Done!");
        System.out.println("Successfully loaded " + numUrls + " urls and " +
                           data.size() + " keywords.");
    }

    //Adds given url with corresponding keywords in the HashMap
    private static void addUrl(String url, String[] words) {

        // For each word in the list of words
        for (String word : words) {
            // Create a new array list
            ArrayList<String> urls = new ArrayList<>();
            // If the word is not already in the hashmap
            if (!data.has(word)) {
                urls.add(url); // Add the provided url to the array list
                data.insert(
                    word,
                    urls); // Insert the word and the array list as a <key, value> pair
            } else { // If the word is already in the map
                urls = data.get(
                    word); // Obtain the array list associated with thhe word
                urls.add(url);        // Add the provided url to the array list
                data.put(word, urls); // Update the urls array list for the word
            }
        }
    }

    // Directs user input to the appropriate helper method to produce the correct search result for a keyword
    private static void performSearch(String word) {

        ArrayList<String> urls = new ArrayList<String>();

        //Perform the actual search
        if (data.has(word)) {
            urls = data.get(word);
        } else {
            urls.clear();
        }

        //Print the results
        System.out.println("\nRESULTS:");
        for (int i = 0; i < urls.size(); i++) {
            System.out.println((i + 1) + ": " + urls.get(i));
        }
        System.out.println(urls.size() + " search results.");
    }
}
