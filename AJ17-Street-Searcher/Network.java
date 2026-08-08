import java.util.*;
//a "Network" is essentially just a weighted graph
public interface Network<T, W> extends Graph<T> {
    /** adds an edge to the graph with the given weight**/
    public void addEdge(T source, T destination, W weight);

    /** gets the edges of a given vertex
  * @param vertex of type T
  * @return the edges of the given vertex in the form of a HashMap. * The keys are verticies and the values are weights.
  **/
    public HashMap<T, W> getEdges(T vertex);
}
