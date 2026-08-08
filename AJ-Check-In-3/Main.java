import java.util.*;
import java.io.*;

class Main {
    public String name;
    public static void main(String[] args) {

        ///////////////////////////
        // SUSTAINABLE PROGRAMMING
        //////////////////////////

        //Sustainable-1,2: What is a nested class? What are the two main types of nested classes? What are their major differences?

        //Sustainable-3: Write a static nested class called Riddle that stores a riddle and an answer. Instantiate the class and print out the riddle and the answer in the main method.
        Riddle joke1 = new Riddle("What's brown and sticky?", "A stick");
        System.out.println("Q: " + joke1.riddle);
        System.out.println("A: " + joke1.answer);

        //Sustainable-4: What are generics? Why are they useful?

        //Sustainable-5: Complete the generic class Triple that stores objects of three generic types. Write a constructor and a toString() method. Then instantiate your class in the main method to test it.
        Triple<String, Boolean, Integer> triple = new Triple<>("a", true, 10);
        System.out.println(triple);

        //Sustainable-6: What is an exception? What happens when an exception is thrown?

        //Sustainable-7,8: Throw a FalseAlarmException between setting the variable state and printing it out. Then catch the exception with a try-catch block.
        String state = "";
        try {
            state = "Everything is fine";
            throw new FalseAlarmException();
        } catch (Exception ex) {
            // false alarm!
        }
        System.out.println(state);

        //Sustainable-9: Say we’re trying to get user input with a scanner. If a user types a string when the scanner is expecting an integer, an exception will be thrown and the program will end. Modify the code below so that the program asks for a number until it receives one. Note: Once an exception is thrown you will need to clear the scanner of input by calling scanner.nextLine();

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while (!done) {
            try {
                System.out.print("Enter a number: ");
                int number = scanner.nextInt();
                done = true;
            } catch (Exception ex) {
                scanner.nextLine();
            }
        }

        ///////////////////////////
        // LINKED LISTS
        //////////////////////////

        // LinkedList-1: What is a singly-linked list? Compared to an ArrayList, what advantages and disadvantages does it have?

        // LinkedList-2: Without looking at any previous code, complete the Node inner class that has been provided in the starter code. When you’re finished, compare it to what you wrote previously.

        // LinkedList-3: Let's extend our LinkedList class. Write an indexOf() method that takes in an item and returns the index of the item in the list. Return -1 if the item doesn't exist in the list.

        //LinkedList-3: Create a LinkedList that contains at least 5 different strings. Test your method by looking up strings that do and don't exist in the list!
        LinkedList<String> list = new LinkedList<>();
        list.insertBack("apple");
        list.insertBack("banana");
        list.insertBack("strawberry");
        list.insertBack("mango");
        list.insertBack("blueberry");

        System.out.println(list.indexOf("blueberry"));
        System.out.println(list.indexOf("not a fruit"));

        // LinkedList-4: What is a doubly linked list? Compared to a singly linked-list, what advantages and disadvantages does it have?

        // LinkedList-5: Modify the Node inner class so that it could be used in a doubly linked list. When you’re finished, compare it to what you wrote previously.

        // LinkedList-6: Let's extend our DoublyLinkedList class. Write a remove() method that takes in an item and removes it from the doubly-linked list. Return true if an item is removed, and false if the item isn't in the list.

        // LinkedList-6: Create a DoublyLinkedList of integers from 0-10. Test your method by printing the list and the length of the list before and after the deletion.
        DoublyLinkedList<Integer> list2 = new DoublyLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list2.insertBack(i);
        }
        System.out.println(list2.remove(5));
        System.out.println(list2.remove(5));
        System.out.println(list2);
        System.out.println(list2.getLength());

        ///////////////////////////
        // STACKS
        //////////////////////////

        // Stacks-1: What is a stack? What operations does it have? What does it mean to be LIFO?

        // Stacks-2,3: Push every letter of the word "toad" onto a stack. Pop from the stack until it only contains the word "to".
        Stack<Character> letters = new Stack<>();
        letters.push('t');
        letters.push('o');
        letters.push('a');
        letters.push('d');
        letters.pop();
        letters.pop();

        // Stacks-4: What data structures can be used to implement a stack?
        //ANSWER: an array or a list (ArrayList or LinkedList)

        // Stacks-5: Write a method that takes in a string of key presses and returns the word that the key presses produce. A "#" in the string of key presses represents hitting the backspace button.
        System.out.println(applyBackspaces("ab#cdef##c"));

        ///////////////////////////
        // QUEUES
        //////////////////////////

        // Queue-1: What is a queue? What operations does it have? What does it mean to be FIFO?

        // Queue-2: Create a queue that stores the names of five colors:
        // “green”, “blue”, “red”, “orange”, “yellow”
        Queue<String> colors = new java.util.LinkedList<>();
        colors.add("green");
        colors.add("blue");
        colors.add("red");
        colors.add("orange");
        colors.add("yellow");
        System.out.println(colors);

        // Queue-2,3: Deque and enqueue until the colors are arranged in rainbow order: “red”, “orange”, “yellow”, “green”, “blue”.
        colors.add(colors.remove());
        colors.add(colors.remove());
        System.out.println(colors);

        // Queue-4: What data structures can be used to implement a queue?
        //ANSWER: an array or a list (ArrayList or LinkedList)

        // Queue-5: What is a priority queue? How is it different from a queue?

        // Queue-6,7: Use a priority queue to create a sorted list of 10 random numbers. Then dequeue and print every item in the queue!
        PriorityQueue<Integer> numbers = new PriorityQueue<>();
        for (int i = 0; i < 10; i++) {
            int num = (int)(Math.random() * 50);
            numbers.add(num);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(numbers.remove() + " ");
        }

        // Queue-8: Can you think of some situations when queues may be useful?
        //ANSWER: Whenever you need to implement something first come first served. A queue acts as a line for things to pile up and wait. Additionally, prioirty queues can be used to sort by priority

        ///////////////////////////
        // MAPS
        //////////////////////////

        // Map-1: What is a map?

        // Map-2: Henry wants to create a map that pairs his friend’s names with their ages. Combine the two parallel lists into a map!

        String[] names = {"Joe",     "Phil",     "Cheyenne", "Josh",
                          "Ryan",    "Samantha", "Nate",     "Addie",
                          "Nichole", "Raegan",   "Caroline"};
        int[] ages = {22, 23, 21, 18, 18, 25, 24, 22, 21, 20, 19};

        Map<String, Integer> namesToAges = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            namesToAges.put(names[i], ages[i]);
        }

        // Map-3,4: Iterate through the map and print out each name and age on a separate line.
        for (String name : namesToAges.keySet()) {
            System.out.println(name + ": " + namesToAges.get(name));
        }

        // Map-5: Check if Henry’s friend “Kyle” is in the map. If he is, print “Kyle is in the map!”. If he’s not, add him to the map. Kyle is 22 years old.
        if (namesToAges.containsKey("Kyle")) {
            System.out.println("Kyle is in the map!");
        } else {
            namesToAges.put("Kyle", 19);
        }

        // Map-6: Henry has a second friend named Joe, but he’s 21 years old. Add this second Joe to the map.
        namesToAges.put("Joe", 25);
        //Map-6: Now that you’ve added the second Joe, print the map. What do you notice? Why is this the case?
        System.out.println(namesToAges);

        // Map-7: Can you think of some situations when maps may be useful?
        //ANSWER: Anytime you need to store pairs of data
    }

    private static class Riddle {
        private String riddle;
        private String answer;
        public Riddle(String riddle, String answer) {
            this.riddle = riddle;
            this.answer = answer;
        }
    }

    private static class Triple<A, B, C> {
        public A one;
        public B two;
        public C three;

        public Triple(A one, B two, C three) {
            this.one = one;
            this.two = two;
            this.three = three;
        }

        @Override
        public String toString() {
            return one.toString() + " " + two.toString() + " " +
                three.toString();
        }
    }

    private class Node<T> {
        private T data;
        private Node<T> previous;
        private Node<T> next;

        //default constructor
        public Node() {
        }
        //constructor with data
        public Node(T data) {
            this.data = data;
        }
    }

    public static String applyBackspaces(String message) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '#' && !stack.isEmpty()) {
                stack.pop();
            } else {
                stack.push(message.charAt(i));
            }
        }
        return stack.toString();
    }
}
