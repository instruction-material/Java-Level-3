import java.util.ArrayList;
import java.util.List;

/**
  An implementation of a directed graph using incidence lists
  for sparse graphs where most things aren't connected.
  @param <V> Vertex element type.
  @param <E> Edge element type.
*/
public class SparseGraph<V, E> implements Graph<V, E> {

    // Class for a vertex of type V
    private final class VertexNode<V> implements Vertex<V> {
        V data;
        Graph<V, E> owner;
        List<Edge<E>> outgoing;
        List<Edge<E>> incoming;
        Object label;
        boolean visited;
        double totalCost;

        VertexNode(V v) {
            this.data = v;
            this.outgoing = new ArrayList<>();
            this.incoming = new ArrayList<>();
            this.label = null;
            this.visited = false;
            this.totalCost = 0;
        }

        @Override
        public V get() {
            return this.data;
        }

        @Override
        public void put(V v) {
            this.data = v;
        }
    }

    //private final class VertexWrapper<V> implements

    //Class for an edge of type E
    private final class EdgeNode<E> implements Edge<E> {
        E data;
        Graph<V, E> owner;
        VertexNode<V> from;
        VertexNode<V> to;
        Object label;

        // Constructor for a new edge
        EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
            this.from = f;
            this.to = t;
            this.data = e;
            this.label = null;
        }

        @Override
        public E get() {
            return this.data;
        }

        @Override
        public void put(E e) {
            this.data = e;
        }
    }

    private List<Vertex<V>> vertices;
    private List<Edge<E>> edges;

    /** Constructor for instantiating a graph. */
    public SparseGraph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    // Checks vertex belongs to this graph
    private void checkOwner(VertexNode<V> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Checks edge belongs to this graph
    private void checkOwner(EdgeNode<E> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Converts the vertex back to a VertexNode to use internally
    private VertexNode<V> convert(Vertex<V> v) throws PositionException {
        try {
            VertexNode<V> gv = (VertexNode<V>)v;
            this.checkOwner(gv);
            return gv;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    private EdgeNode<E> convert(Edge<E> e) throws PositionException {
        try {
            EdgeNode<E> ge = (EdgeNode<E>)e;
            this.checkOwner(ge);
            return ge;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    @Override
    public Vertex<V> insert(V v) {
        // TODO
    }

    @Override
    public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
        throws PositionException, InsertionException {
        // TODO
    }

    /**Sets the total cost from start to current vertex.
  *@param v the current vertex
  *@param cost the cost of the vertex pathway
  *@throws PositionException if v is invalid
  */
    public void totalCost(Vertex<V> v, double cost) {
        // TODO
    }

    /**Method to return the totalCost from start to v.
  *@param v the current vertex
  *@throws PositionException if v is invalid
  *@return the total cost of v's pathway
  */
    public double totalCost(Vertex<V> v) throws PositionException {
        // TODO
    }

    @Override
    public V remove(Vertex<V> v) throws PositionException, RemovalException {
        // TODO
    }

    @Override
    public E remove(Edge<E> e) throws PositionException {
        // TODO
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        // TODO
    }

    private Iterable<Edge<E>> returnIter(List<Edge<E>> list) {
        // TODO
    }

    @Override
    public Vertex<V> from(Edge<E> e) throws PositionException {
        // TODO
    }

    @Override
    public Vertex<V> to(Edge<E> e) throws PositionException {
        // TODO
    }

    @Override
    public void label(Vertex<V> v, Object l) throws PositionException {
        // TODO
    }

    @Override
    public void label(Edge<E> e, Object l) throws PositionException {
        // TODO
    }

    @Override
    public Object label(Vertex<V> v) throws PositionException {
        // TODO
    }

    @Override
    public Object label(Edge<E> e) throws PositionException {
        // TODO
    }

    @Override
    public void clearLabels() {
        // TODO
    }

    /**Method to change the visited status for a given vertex.
  *@param v the given vertex
  *@throws PositionException if v is invalid
  *@param visit the new status of visited
  */
    public void visited(Vertex<V> v, boolean visit) throws PositionException {
        // TODO
    }

    /**Method to return the visited status for a given vertex.
  *@param v the given vertex
  *@throws PositionException if v is invalid
  *@return the visited status of the vertex
  */
    public boolean visited(Vertex<V> v) throws PositionException {
        // TODO
    }

    /**FOR PRINTING: NOT FOR STUDENTS*/
    private String vertexString(Vertex<V> v) {
        return "\"" + v.get() + "\"";
    }

    private String verticesToString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<V> v : this.vertices) {
            sb.append("  ").append(vertexString(v)).append("\n");
        }
        return sb.toString();
    }

    private String edgeString(Edge<E> e) {
        return String.format("%s -> %s [label=\"%s\"]",
                             this.vertexString(this.from(e)),
                             this.vertexString(this.to(e)), e.get());
    }

    private String edgesToString() {
        String edgs = "";
        for (Edge<E> e : this.edges) {
            edgs += "    " + this.edgeString(e) + "\n";
        }
        return edgs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n")
            .append(this.verticesToString())
            .append(this.edgesToString())
            .append("}");
        return sb.toString();
    }
}
