import java.util.List;
import java.util.ArrayList;

public class HashMap<K extends Comparable<K>, V> implements Map<K, V> {

  private int numNodes;
  private int tableSize;
  private Object[] hashTable;
  
  private static final int[] PRIMES = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,97,193,389,769,1543,3079,6151,12289,24593,49157,98317,139291,196613,259537,299993,350213,393241,444863,499979,550279,599999,652153,700001,786433,851359,900001,950473,999983,1572869,3145739,6291469,12582917,25165843,50331653,100663319,201326611,402653189,805306457,1610612741};

  private class Node {
    private K key;  
    private V value;
    private boolean tombStone; //marks the node as deleted
    public Node(K k, V v) {
      this.key = k;
      this.value = v;
      this.tombStone = false;
    }
    @Override
    public String toString() {
      return this.key + ": " + this.value;
    }
  }

  //CONSTRUCTOR -----------------------------------------------
  public HashMap() {
    //TODO:
  }

  //HASH FUNCTION ---------------------------------------------
  private int hash(K key) {
    //TODO
    //returns a unique hashcode 
    //make sure to compress the code into the bounds of the table (this can easily be done with the "mod" operator(%))
    return -1;
  }

  //LOAD FACTOR
  private double loadFactor(){
    //returns the "load factor" of the hashcode by dividing the number of items by the total number of slots
    return 0.0;
  }

  //GET INDEX METHOD ------------------------------------------
  private int getIndex(K key, boolean inserting) {
    //TODO:
    //Given a particular key, this method returns the "index" where we can insert, remove, put, or get. In this case, an "index" is different from a "hashcode" because an index is always unique: it starts with a hashcode, but if there are collisions it uses linear probing to find a unique, open slot.

    //1. First it hashes the key to get a hashcode
    //2. Then it uses the hashcode to get the node from the table
    //3. Finally if there are collisions, it uses linear probing to find an open address (index).

    //If the method is used for inserting (denoted by the parameter "inserting"), stop probing and return the index if a tombstone is encountered. If it's used for any other purpose (deleting, putting, getting, etc) keep probing when you encounter a tombstone (the item you're looking for might be further in the table).
    return -1;
  }

  // MAP METHODS -----------------------------------------------
  //inserts a key-value pair
  @Override
  public void insert(K key, V value) {
    //TODO
  }

  //removes a key-value pair
  @Override
  public V remove(K key) {
    //TODO
    return null;
  }

  //updates value associated with key
  @Override
  public void put(K key, V value) {
    //TODO
  }

  //get value associated with key
  @Override
  public V get(K key) {
    //TODO
    return null;
  }

  //returns whether the hash map has the value or not
  @Override
  public boolean has(K key) {
    //TODO
    return false;
  }

  //returns the number of nodes that have been added
  @Override
  public int size() {
    //TODO
    return -1;
  }

  //REHASH METHOD -------------------------------------------
  //rehashes the hashtable into a bigger hashtable
  private void rehash() {
    //TODO
  }
  //---------------------------------------------------------
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    for (int i = 0; i < tableSize; i++) {
      Node node = (Node)hashTable[i];
      if (node != null && !node.tombStone) {
        stringBuilder.append(node.toString());
        if(i < tableSize-1){
          stringBuilder.append(", ");
        }
      }
    }
    stringBuilder.append("\b\b}");
    return stringBuilder.toString();
  }
}