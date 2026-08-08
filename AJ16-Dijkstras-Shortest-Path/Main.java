import java.util.*;
import java.lang.*;
import java.io.*;

class Main {
    public static void main(String[] args) {

        //adjacency matrix
        int[][] graph = new int[][] {/*
            0  1  2  3  4
    /*0*/
                                     {0, 0, 3, 2, 0},
                                     /*1*/ {0, 0, 0, 6, 1},
                                     /*2*/ {3, 0, 0, 2, 5},
                                     /*3*/ {2, 6, 2, 0, 0},
                                     /*4*/ {0, 1, 5, 0, 0}};

        System.out.println("\nDijkstra's Shortest Path:\n");
        ShortestPathTree tree = new ShortestPathTree();
        tree.dijkstra(graph, 0);
    }
}
