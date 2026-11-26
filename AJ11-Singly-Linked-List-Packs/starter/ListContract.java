interface ListContract<T> {
    int getLength();
    boolean isEmpty();
    void insertFront(T value);
    void insertBack(T value);
    void removeFront();
    void removeBack();
    T get(int index);
    boolean clearAll();
}
