import java.util.*;

public class Graph<T> {

    private Map<T, ArrayList<T>> map = new HashMap<>();
    private int edges = 0;
    private int vertices = 0;

    //GETTERS -------------------------------------------------------
    public int getVertexCount() {
        return vertices;
    }
    public int getEdgesCount() {
        return edges;
    }

    //HAS METHODS ----------------------------------------------------
    public boolean hasVertex(T vertex) {
        return map.containsKey(vertex);
    }
    public boolean hasEdge(T source, T destination) {
        if (hasVertex(source)) {
            return map.get(source).contains(destination);
        } else {
            return false;
        }
    }

    //ADD METHODS ----------------------------------------------------
    public void addVertex(T vertex) {
        if (!hasVertex(vertex)) {
            map.put(vertex, new ArrayList<T>());
            vertices++;
        }
    }
    public void addEdge(T source, T destination) {

        //add the vertices (if possible)
        addVertex(source);
        addVertex(destination);

        //add the edge (if possible)
        if (!hasEdge(source, destination)) {
            //add the edge in both directions
            //since the graph is undirected
            map.get(source).add(destination);
            map.get(destination).add(source);
            edges++;
        }
    }

    //TOSTRING -------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (T vertex : map.keySet()) {
            builder.append(vertex.toString() + ": ");
            for (T edge : map.get(vertex)) {
                builder.append(edge.toString() + " ");
            }
            builder.append("\n");
        }
        return (builder.toString());
    }

    //EULERIAN METHODS ------------------------------------------------

    // very basic test function
    public void test() {
        int response = isEulerian();
        if (response == 0) {
            System.out.println("Not Eulerian");
        } else if (response == 1) {
            System.out.println("Has a Euler path");
        } else {
            System.out.println("Has a Euler cycle");
        }
    }

    /* The function returns one of the following values
      0 --> If graph is not Eulerian
      1 --> If graph has an Euler path (Semi-Eulerian)
      2 --> If graph has an Euler Circuit (Eulerian)  */
    public int isEulerian() {

        //if the graph is not connected, it's not Eulerian
        if (isConnected() == false) {
            return 0;
        }

        //get the number of vertices...
        //...that have an odd degree
        int odd = 0;
        for (T vertex : map.keySet()) {
            if (map.get(vertex).size() % 2 == 1) {
                odd++;
            }
        }

        //if odd count is more than 2, then NOT Eulerian
        //if odd count is 2, then Semi-Eulerian.
        //if odd count is 0, then Eulerian
        if (odd > 2) {
            return 0;
        } else if (odd == 2) {
            return 1;
        } else {
            return 2;
        }
    }
    public boolean isConnected() {
        //if there are no vertices, return true!
        if (vertices == 0) {
            return false;
        }

        Map<T, Boolean> visited = new HashMap<>();

        //intialize the map with all false values
        for (T vertex : map.keySet()) {
            visited.put(vertex, false);
        }

        //visit as many verticies as possible
        T firstVertex = map.keySet().iterator().next();
        visit(firstVertex, visited);

        //if every vertex has been visited, return True!
        for (T vertex : visited.keySet()) {
            if (visited.get(vertex) == false) {
                return false;
            }
        }
        return true;
    }
    public void visit(T vertex, Map<T, Boolean> visited) {

        visited.put(vertex, true);

        //visit every vertex in the adjacency list
        ArrayList<T> adjacencyList = map.get(vertex);
        for (T neighbor : adjacencyList) {

            //only visit the vertex if it hasn't already been visited
            if (visited.get(neighbor) == false) {
                visit(neighbor, visited);
            }
        }
    }
}
