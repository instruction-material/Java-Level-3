import java.util.*;

public class DirectedGraph<T> implements Graph<T> {

    private Map<T, ArrayList<T>> map = new HashMap<>();
    private int numEdges = 0;

    //GETTERS -------------------------------------------------------
    public int getVertexCount() {
        return map.size();
    }
    public int getEdgesCount() {
        return numEdges;
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
        }
    }
    public void addEdge(T source, T destination) {

        //add the vertices (if possible)
        addVertex(source);
        addVertex(destination);

        //add the edge (if possible)
        if (!hasEdge(source, destination)) {
            map.get(source).add(destination);
            numEdges++;
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
}
