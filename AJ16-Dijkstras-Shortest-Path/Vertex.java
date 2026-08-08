import java.util.*;

public class Vertex<T> implements Comparable<Vertex<T>> {

    T label;
    int distance;
    Vertex previous;

    public Vertex(T label) {
        this.label = label;
        this.distance = -1;
        this.previous = null;
    }

    public T getLabel() {
        return label;
    }
    public void setlabel(T label) {
        this.label = label;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public Vertex getPrevious() {
        return previous;
    }
    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    @Override
    public int compareTo(Vertex<T> other) {
        int result = distance - other.getDistance();
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Vertex: " + label);
        result.append(" Distance: " + distance);
        result.append(" Path: " + getShortestPath());
        return result.toString();
    }

    public String getShortestPath() {
        if (previous == null) {
            return label.toString();
        }
        return label.toString() + ", " + previous.getShortestPath();
    }
}
