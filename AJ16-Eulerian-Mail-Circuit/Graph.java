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
        if (map.isEmpty()) return 0;
        if (edges == 0) return 2;

        Map<T, Boolean> visited = new HashMap<>();
        for (var entry : map.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                visit(entry.getKey(), visited);
                break;
            }
        }

        int odd = 0;
        for (var entry : map.entrySet()) {
            int degree = entry.getValue().size();
            if (degree != 0 && !Boolean.TRUE.equals(visited.get(entry.getKey()))) return 0;
            odd += degree % 2;
        }
        if (odd == 0) return 2;
        return odd == 2 ? 1 : 0;
    }

    public boolean isConnected() {
        if (map.isEmpty()) return false;
        Map<T, Boolean> visited = new HashMap<>();
        visit(map.keySet().iterator().next(), visited);
        return visited.size() == map.size();
    }

    public void visit(T vertex, Map<T, Boolean> visited) {
        Deque<T> toVisit = new LinkedList<>();
        visited.put(vertex, true);
        toVisit.addLast(vertex);
        while (!toVisit.isEmpty()) {
            for (T neighbor : map.get(toVisit.removeFirst())) {
                if (!Boolean.TRUE.equals(visited.get(neighbor))) {
                    visited.put(neighbor, true);
                    toVisit.addLast(neighbor);
                }
            }
        }
    }
}
