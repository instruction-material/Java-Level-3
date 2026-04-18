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
    private boolean tombStone;
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
    this.tableSize = 47;
    this.hashTable = new Object[this.tableSize];
  }

  //HASH FUNCTION ---------------------------------------------
  private int hash(K key) {
    return Math.abs(key.hashCode() % this.tableSize);
  }

  //LOAD FACTOR------------------------------------------------
  private double loadFactor(){
    return (double) numNodes / tableSize;
  }

  //GET INDEX METHOD ------------------------------------------
  private int getIndex(K key, boolean inserting) {

    int index = hash(key);
    Node node = getNode(index);

    //linear probe 
    while(node != null && !key.equals(node.key)){
      if(inserting && node.tombStone){
        break;
      } 
      index = (index + 1) % tableSize;
      node = getNode(index);
    }

    return index;
  }


  //GET NODE METHODS ------------------------------------------
  //returns a node, possibly null or marked as removed
  private Node getNode(int index) {
    Node node = (Node) hashTable[index];
    return node;
  }

  //gets a Node in a particular table (useful for rehashing)
  private Node getNode(int index, Object[] table) {
    // assists with rehashing into a bigger table
    Node n = (Node) table[index];
    return n;
  }

  //returns a node, but if it's null or marked as removed, it throws an exception
  private Node getExistingNode(int index) {
    Node node = (Node) hashTable[index];
    if (node == null || node.tombStone) {
      throw new IllegalArgumentException("ERROR: cannot find key");
    }
    return node;
  }

  // MAP METHODS -----------------------------------------------
  //inserts a key-value pair
  @Override
  public void insert(K key, V value) {
    
    //rehash if neccessary
    if(loadFactor() >= 0.5) {
      rehash();
    }

    //add the new node
    int index = getIndex(key, true);
    Node node = new Node(key, value);
    hashTable[index] = node;
    this.numNodes++;
  }

  //removes a key-value pair
  @Override
  public V remove(K key) {
    Node node = getExistingNode(getIndex(key, false));
    //mark as deleted with the tombstone,
    //but don't actually delete
    node.tombStone = true;
    this.numNodes--;
    return node.value;
  }

  //updates value associated with key
  @Override
  public void put(K key, V value) {
    Node node = getExistingNode(getIndex(key, false));
    node.value = value;
  }

  //get value associated with key
  @Override
  public V get(K key) {
    Node node = getExistingNode(getIndex(key, false));
    return node.value;
  }

  //returns whether the hash map has the value or not
  @Override
  public boolean has(K key) {
    try{
      Node node = getExistingNode(getIndex(key, false));
    }catch(IllegalArgumentException ex){
      return false;
    }
    return true;
  }

  //returns the number of nodes that have been added
  @Override
  public int size() {
    return this.numNodes;
  }

  //REHASH METHOD -------------------------------------------
  private void rehash() {
    System.out.println("REHASH!!");
    Object[] oldTable = hashTable;
    int oldtableSize = tableSize; 
    tableSize *= 2; 
    numNodes = 0;

    //update the tablesize to closest prime number
    int i = 0;
    while(tableSize > PRIMES[i]) {
      i++;
    }
    tableSize = PRIMES[i];

    //create a new array with the bigger size
    hashTable = new Object[this.tableSize];

    //for each element in the old array
    for (int j = 0; j < oldtableSize; j++) {
      Node node = getNode(j, oldTable); 

      //if the node exists and is not "removed"
      if (node != null && !node.tombStone) {
        insert(node.key,node.value);
      }
    }
  }

  /** FOR PRINTING THE HASH MAP -- NOT FOR STUDENTS TO CODE*/
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    for (int i = 0; i < tableSize; i++) {
      Node node = getNode(i);
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