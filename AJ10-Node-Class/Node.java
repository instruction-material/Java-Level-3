public class Node<T> {
    T data; // data of type T

    //constructor
    public Node(T data) {
        this.data = data;
    }
    //getter
    public T get() {
        return data;
    }
    //setter
    public void set(T data) {
        this.data = data;
    }
    //toString()
    public String toString() {
        return data.toString();
    }
}
