public interface Map<K, V> {
  void insert(K k, V v) throws IllegalArgumentException;
  V remove(K k) throws IllegalArgumentException;
  void put(K k, V v) throws IllegalArgumentException;
  V get(K k) throws IllegalArgumentException;
  boolean has(K k);
  int size();
}
