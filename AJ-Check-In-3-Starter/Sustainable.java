import java.util.*;
import java.io.*;

public class Sustainable {
    public static void main(String[] args) {
        ///////////////////////////
        // SUSTAINABLE PROGRAMMING
        //////////////////////////

        //Sustainable-1,2: What is a nested class? What are the two main types of nested classes? What are their major differences?

        //Sustainable-3: Write a static nested class called Riddle that stores a riddle and an answer. Instantiate the class and print out the riddle and the answer in the main method.

        //Sustainable-4: What are generics? Why are they useful?

        //Sustainable-5: Complete the generic class Triple that stores objects of three generic types. Write a constructor and a toString() method. Then instantiate your class in the main method to test it.

        //Sustainable-6: What is an exception? What happens when an exception is thrown?

        //Sustainable-7,8: Write a simple FalseAlarmException class. Then throw a FalseAlarmException between setting the variable "state" and printing it out. Finally, catch the exception with a try-catch block.
        String state = "";
        state = "Everything is fine";
        System.out.println(state);

        //Sustainable-9: Say we’re trying to get user input with a scanner. If a user types a string when the scanner is expecting an integer, an exception will be thrown and the program will end. Modify the code below so that the program asks for a number until it receives one. Note: Once an exception is thrown you will need to clear the scanner of input by calling scanner.nextLine();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
    }

    private static class Triple<A, B, C> {
        // YOUR CODE HERE
    }
}
