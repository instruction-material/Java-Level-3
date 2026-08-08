import java.util.*;

class Main {
    public static void main(String[] args) {

        Queue<String> queue = new LinkedList<>();
        String[] kids = {"Kyle",  "Johnny",   "Carlos", "Brian",    "Asia",
                         "Chloe", "Melissa",  "Finn",   "Brooklyn", "Kesha",
                         "Gavin", "Isabella", "Andre",  "Matias",   "Ebony",
                         "Devin", "James",    "Shawn"};
        queue.addAll(Arrays.asList(kids));

        // regular
        System.out.println("\nRegular:\n" + queue);

        // reverse
        reverse(queue);
        System.out.println("\nReversed:\n" + queue);

        // sort alphabetically
        alphabetize(queue);
        System.out.println("\nAlphabetized:\n" + queue);
    }

    private static void reverse(Queue<String> queue) {

        // push everything onto a stack
        Stack<String> stack = new Stack<>();
        while (queue.size() > 1) {
            stack.push(queue.remove());
        }

        //add everything back into the queue
        while (stack.size() > 0) {
            queue.add(stack.pop());
        }
    }
    private static void alphabetize(Queue<String> queue) {
        PriorityQueue<String> pq = new PriorityQueue<>();

        // add everything to a priority queue
        while (queue.size() > 0) {
            pq.add(queue.remove());
        }

        //add everything back into the queue
        while (pq.size() > 0) {
            queue.add(pq.remove());
        }
    }
}
