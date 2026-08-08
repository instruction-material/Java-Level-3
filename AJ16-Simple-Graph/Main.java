import java.util.*;

class Main {
    public static void main(String args[]) {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();

        g.addEdge(0, 1);
        g.addEdge(0, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        System.out.println("\nGraph:\n" + g.toString());

        // gives the no of vertices in the graph.
        System.out.println(g.getVertexCount() + " vertices");

        // gives the no of edges in the graph.
        System.out.println(g.getEdgesCount() + " edges");

        // tells whether the edge is present or not.
        System.out.println("Does the graph contain an edge between 3 and 4? " +
                           g.hasEdge(3, 4));

        // tells whether vertex is present or not
        System.out.println("Does the graph contain 5 as a vertex? " +
                           g.hasVertex(5));
    }
}
