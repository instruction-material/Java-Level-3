import java.util.*;
import java.lang.*;
import java.io.*;

public class ShortestPathTree {

    int[][] graph;
    ArrayList<Vertex<Integer>> vertices;
    PriorityQueue<Vertex<Integer>> unvisited;

    public void dijkstra(int[][] graph, int source) {

        this.graph = graph;
        vertices = new ArrayList<>();
        unvisited = new PriorityQueue<>();

        //initialize vertices
        //set all distances to infinity (max value)
        for (int i = 0; i < graph.length; i++) {
            Vertex<Integer> vertex = new Vertex<>(i);
            vertex.setDistance(Integer.MAX_VALUE);
            vertices.add(vertex);
        }

        //set the distance of the source to zero
        vertices.get(source).setDistance(0);

        //fill the priority queue
        for (Vertex vertex : vertices) {
            unvisited.add(vertex);
        }

        //run the algorithm
        Vertex<Integer> smallest;
        while (!unvisited.isEmpty()) {
            smallest = unvisited.poll();
            updateNeighborDistances(smallest);
        }

        //print the verticies
        printSolution();
    }
    public void updateNeighborDistances(Vertex<Integer> smallest) {

        int index = smallest.getLabel();
        //for each vertex in the graph
        for (int neighborIndex = 0; neighborIndex < graph[index].length;
             neighborIndex++) {

            //if there is an edge connecting the two vertices
            if (graph[index][neighborIndex] > 0) {
                //get the neighbor
                Vertex neighbor = vertices.get(neighborIndex);
                //if the neighbor hasn't been visited
                if (unvisited.contains(neighbor)) {
                    //compare the neighbor's current and alternate distances
                    int current = neighbor.getDistance();
                    int alternate =
                        smallest.getDistance() + graph[index][neighborIndex];
                    //if the alternate is smaller than the current...
                    if (alternate < current) {
                        //replace the current with the alternate
                        neighbor.setDistance(alternate);
                        neighbor.setPrevious(smallest);
                        //remove and add the neighbor...
                        //...to update it's priority in the queue
                        unvisited.remove(neighbor);
                        unvisited.add(neighbor);
                    }
                }
            }
        }
    }

    public void printSolution() {
        for (Vertex<Integer> vertex : vertices) {
            System.out.println(vertex);
        }
    }
}
