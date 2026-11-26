import java.util.ArrayDeque;

public class Vertex<T> implements Comparable<Vertex<T>> {
    T label;
    long distance;
    Vertex<T> previous;

    public Vertex(T label) {
        this.label = label;
        distance = Long.MAX_VALUE;
    }

    public T getLabel() {
        return label;
    }

    public void setlabel(T label) {
        this.label = label;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public Vertex<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex<T> previous) {
        this.previous = previous;
    }

    @Override
    public int compareTo(Vertex<T> other) {
        return Long.compare(distance, other.distance);
    }

    @Override
    public String toString() {
        String cost = distance == Long.MAX_VALUE ? "unreachable" : Long.toString(distance);
        return "Vertex: " + label + " Distance: " + cost + " Path: " + getShortestPath();
    }

    public String getShortestPath() {
        if (distance == Long.MAX_VALUE) return "[]";

        var path = new ArrayDeque<String>();
        for (Vertex<T> vertex = this; vertex != null; vertex = vertex.previous) {
            path.addFirst(String.valueOf(vertex.label));
        }
        return String.join(", ", path);
    }
}
