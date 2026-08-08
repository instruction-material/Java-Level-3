import java.io.*;
import java.util.*;
import java.util.LinkedList;

class Main {
    public static void main(String args[]) {
        System.out.println("\nWelcome to the Juni Mail Service!");
        System.out.println(
            "Our mail routes visit every street once! No more, no less!");
        System.out.println("\nA Eulerian Path is a path in which every edge "
                           + "is used only once.");
        System.out.println(
            "A Eulerian Cycle is a path in which every edge is used only "
            + "once, AND you finish at the same vertex that you started.");

        System.out.println("If you give me a graph of locations, I'll tell "
                           + "you what kind of paths are possible:\n");

        Graph<Integer> g1 = new Graph<>();
        g1.addEdge(1, 0);
        g1.addEdge(0, 2);
        g1.addEdge(2, 1);
        g1.addEdge(0, 3);
        g1.addEdge(3, 4);
        System.out.print("Graph 1: ");
        g1.test();
        System.out.println(g1);

        Graph<Integer> g2 = new Graph<>();
        g2.addEdge(1, 0);
        g2.addEdge(0, 2);
        g2.addEdge(2, 1);
        g2.addEdge(0, 3);
        g2.addEdge(3, 4);
        g2.addEdge(4, 0);
        System.out.print("Graph 2: ");
        g2.test();
        System.out.println(g2);

        Graph<Integer> g3 = new Graph<>();
        g3.addEdge(1, 0);
        g3.addEdge(0, 2);
        g3.addEdge(2, 1);
        g3.addEdge(0, 3);
        g3.addEdge(3, 4);
        g3.addEdge(1, 3);
        System.out.print("Graph 3: ");
        g3.test();
        System.out.println(g3);

        // testing out a cycle
        Graph<Integer> g4 = new Graph<>();
        g4.addEdge(0, 1);
        g4.addEdge(1, 2);
        g4.addEdge(2, 0);
        System.out.print("Graph 4: ");
        g4.test();
        System.out.println(g4);

        // blank graph
        Graph<Integer> g5 = new Graph<>();
        System.out.print("Graph 5: ");
        g5.test();
        System.out.println(g5);
    }
}
