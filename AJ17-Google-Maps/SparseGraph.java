import java.util.ArrayList;
import java.util.List;

/**
  An implementation of a directed graph using incidence lists(?)
  for sparse graphs where most things aren't connected.
  @param <V> Vertex element type.
  @param <E> Edge element type.
*/
public class SparseGraph<V, E> implements Graph<V, E> {

    // Class for a vertex of type V
    private final class VertexNode<V> implements Vertex<V> {
        V data; //String, coordinates
        Graph<V, E> owner;
        List<Edge<E>> outgoing;
        List<Edge<E>> incoming;
        Object
            label; //either null, or if the vertex is along the final path, it's the next edge along the path
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
            //retrun the String coordinates
            return this.data;
        }

        @Override
        public void put(V v) {
            //set the String coordinates
            this.data = v;
        }
    }

    //Class for an edge of type E
    private final class EdgeNode<E> implements Edge<E> {
        E data; //String, name of the street(ex: One_World_Trade_Center--Statue_of_Liberty)

        Graph<V, E> owner;
        VertexNode<V> from;
        VertexNode<V> to;
        Object label; //weight (distance)

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

    //INSERT METHODS ------------------------------------------------

    //insert vertex
    @Override
    public Vertex<V> insert(V v) {
        VertexNode<V> vn = new VertexNode<V>(v); //create new vertex node
        vn.owner = this;
        this.vertices.add(vn); //add to list of vertices
        return vn;             //return new vertex node
    }

    //insert edge
    @Override
    public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
        throws PositionException, InsertionException {

        //Invalid vertex
        VertexNode<V> f = this.convert(from);
        VertexNode<V> t = this.convert(to);

        if (this.convert(from).equals(this.convert(to))) { //self-loop
            throw new InsertionException();
        }

        EdgeNode<E> en =
            new EdgeNode<E>(this.convert(from), this.convert(to), e);
        en.owner = this;

        //duplicate edge
        for (Edge<E> dupe : this.edges) {
            if (this.from(dupe).equals(f) && this.to(dupe).equals(t)) {
                throw new InsertionException();
            }
        }

        //en.owner = this;

        //add new edge node to the outgoing list for from
        this.convert(from).outgoing.add(this.convert(en));
        //add new edge node to the incoming list for to
        this.convert(to).incoming.add(this.convert(en));
        this.edges.add(this.convert(en)); //add edge node to the edges list
        return en;                        //return new edge node
    }

    //REMOVE METHODS -----------------------------------------------

    @Override
    public V remove(Vertex<V> v) throws PositionException, RemovalException {
        VertexNode<V> vn = this.convert(v);

        if (!vn.outgoing.isEmpty() || !vn.incoming.isEmpty()) {
            throw new RemovalException();
        }

        V val = vn.data;
        this.vertices.remove(vn);
        vn.owner = null;
        return val;
    }

    @Override
    public E remove(Edge<E> e) throws PositionException {
        EdgeNode<E> en = this.convert(e);
        Vertex<V> f = this.from(en); //Get the from vertex for edge
        Vertex<V> t = this.to(en);   //Get the to vertex for edge
        this.convert(f).outgoing.remove(en);
        this.convert(t).incoming.remove(en);

        E val = en.data;
        this.edges.remove(en);
        en.owner = null;
        return val;
    }

    //FROM, TO ----------------------------------------------------

    //returns an internal vertexNode from
    @Override
    public Vertex<V> from(Edge<E> e) throws PositionException {
        EdgeNode<E> en = this.convert(e);
        return en.from;
    }

    //returns an internal vertexNode to
    @Override
    public Vertex<V> to(Edge<E> e) throws PositionException {
        EdgeNode<E> en = this.convert(e);
        return en.to;
    }

    //LABEL METHODS --------------------------------------------------

    //set vertex label
    @Override
    public void label(Vertex<V> v, Object l) throws PositionException {
        VertexNode<V> vn = this.convert(v);
        vn.label = l;
    }

    //set edge label
    @Override
    public void label(Edge<E> e, Object l) throws PositionException {
        EdgeNode<E> en = this.convert(e);
        en.label = l;
    }

    //get vertex label
    @Override
    public Object label(Vertex<V> v) throws PositionException {
        VertexNode<V> vn = this.convert(v);
        return vn.label;
    }

    //get edge label
    @Override
    public Object label(Edge<E> e) throws PositionException {
        EdgeNode<E> en = this.convert(e);
        return en.label;
    }

    //clear labels (is this even used?)
    @Override
    public void clearLabels() {
        for (Vertex<V> v : this.vertices) {
            this.convert(v).label = null;
        }
        for (Edge<E> e : this.edges) {
            this.convert(e).label = null;
        }
    }

    //CHECK OWNER METHODS --------------------------------------------
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

    //CONVERT METHODS -----------------------------------------------

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

    //TOTAL COST GETTER SETTER -------------------------------------

    /**Sets the total cost from start to current vertex.
  *@param v the current vertex
  *@param cost the cost of the vertex pathway
  *@throws PositionException if v is invalid
  */
    public void totalCost(Vertex<V> v, double cost) throws PositionException {
        this.convert(v).totalCost = cost;
    }

    /**Method to return the totalCost from start to v.
  *@param v the current vertex
  *@throws PositionException if v is invalid
  *@return the total cost of v's pathway
  */
    public double totalCost(Vertex<V> v) throws PositionException {
        return this.convert(v).totalCost;
    }

    //ITERITABLE METHODS? -------------------------------------------

    //return arrayList of vertices, somehow iteritible
    public Iterable<Vertex<V>> vertices() {
        ArrayList<Vertex<V>> l = new ArrayList<Vertex<V>>(vertices);
        return (Iterable<Vertex<V>>)l;
    }
    //return the given list of edges, but now somehow iteritible
    private Iterable<Edge<E>> returnIter(List<Edge<E>> list) {
        ArrayList<Edge<E>> l = new ArrayList<Edge<E>>(list);
        return (Iterable<Edge<E>>)list;
    }
    //return arrayList of edges, somehow iteritible
    public Iterable<Edge<E>> edges() {
        return this.returnIter(edges);
    }

    //converts the given vertex into an internal vertex
    //then returns the arrayList of outgoing edges
    public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
        return this.returnIter(this.convert(v).outgoing);
    }

    //converts the given vertex into an internal vertex
    //then returns the arrayList of incoming edges
    public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
        return this.returnIter(this.convert(v).incoming);
    }

    //VISITED GET AND SET ----------------------------------------

    /**Method to change the visited status for a given vertex.
  *@param v the given vertex
  *@throws PositionException if v is invalid
  *@param visit the new status of visited
  */
    public void visited(Vertex<V> v, boolean visit) throws PositionException {
        VertexNode<V> vn = this.convert(v);
        vn.visited = visit;
    }

    /**Method to return the visited status for a given vertex.
  *@param v the given vertex
  *@throws PositionException if v is invalid
  *@return the visited status of the vertex
  */
    public boolean visited(Vertex<V> v) throws PositionException {
        VertexNode<V> vn = this.convert(v);
        return vn.visited;
    }

    //TOSTRING METHODS ------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n")
            .append(this.verticesToString())
            .append(this.edgesToString())
            .append("}");
        return sb.toString();
    }

    //VERTICES
    private String verticesToString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<V> v : this.vertices) {
            sb.append("  ").append(vertexString(v)).append("\n");
        }
        return sb.toString();
    }
    private String vertexString(Vertex<V> v) {
        return "\"" + v.get() + "\"";
    }

    //EDGES
    private String edgesToString() {
        String edgs = "";
        for (Edge<E> e : this.edges) {
            edgs += "    " + this.edgeString(e) + "\n";
        }
        return edgs;
    }
    private String edgeString(Edge<E> e) {
        return String.format("%s -> %s [label=\"%s\"]",
                             this.vertexString(this.from(e)),
                             this.vertexString(this.to(e)), e.get());
    }
}
