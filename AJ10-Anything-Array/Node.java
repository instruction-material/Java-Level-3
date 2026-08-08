/**A Node stores data of type "T" **/
public class Node<T> {
    private T data;
    public Node(T data) {
        this.data = data;
    }
    public T get() {
        return data;
    }
    public void set(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return data.toString();
    }
}
