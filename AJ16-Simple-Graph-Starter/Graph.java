public interface Graph<T> {
    public int getVertexCount();
    public int getEdgesCount();
    public boolean hasVertex(T vertex);
    public boolean hasEdge(T source, T destination);
    public void addVertex(T vertex);
    public void addEdge(T source, T destination);
}
