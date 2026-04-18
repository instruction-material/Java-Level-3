class Main {
  public static void main(String[] args) {

    ////////////////////////////////
    //// BINARY SEARCH TREES
    ////////////////////////////////

    /*

    BST-2: Define the following terms:
    -Node: Stores data
    -Edge: Connects adjacent nodes
    -Parent: Nearest ancestor of a child
    -Child: Nearest descendant of a parent
    -Ancestor: Node that comes above
    -Descendant: Node that comes after
    -Degree: The number of children (always 2 or less in a BST)
    -Root: The top node, with no ancestors
    -Leaf: Bottom node with no children
    -Height: The number of edges from the root to a leaf
    -Subtree: A tree within a tree, where one of the nodes is the new root

    //BST-3: Describe the process of depth-first search. Then write out the pre-order, in-order, and post-order traversals for the following tree:

    // ANSWER:
    // Pre-Order: A,B,C,D,E,F,G
    // In-Order: C,B,D,A,F,E,G
    // Post-Order: C,D,B,F,G,E,A

    */

    ////////////////////////////////
    //// HASHTABLES
    ////////////////////////////////

    /*
    HashTables-3: What is a load factor? Calculate the load factors for the following tables:

    1543 slots, 695 nodes = 0.45 
    700,001 slots 420,000 nodes = 0.59 Rehash
    193 slots, 98 nodes = 0.507 Rehash

    */

    ////////////////////////////////
    //// GRAPHS
    ////////////////////////////////

    /* 
    Graph-2: Define the following terms:
    -Vertex: Node in a graph
    -Edge: Connects vertices
    -Circuit: A cycle, a cpath that begins and ends in the same place
    -Directed: Directions on the edges (cant go both ways)
    -Undirected: No directions on the edges and can traverse form either direction
    -Path: An ordering of vertices that can be traversed from one to the next
    -Adjacent Vertices: Vertices directly conncted by an edge
    -Degree: The number of edges coming in or out of a vertex
    -Weighted: Weights on the edges
    */

    //Graph-3: Create an adjacency list from the given graph.
    /*
    0: 3
    1: 2, 5
    2: 6
    3: 7
    4: 1
    5: 2
    6: 5, 7
    7: 4
    */

    //Graph-4: Create an adjacency matrix from the given graph.
    int[][] matrix = new int[][] {/*
            A  B  C  D  E   
    /*A*/ { 0, 1, 1, 1, 3}, 
    /*B*/ { 1, 0, 5, 7, 0}, 
    /*C*/ { 1, 5, 0, 0, 0}, 
    /*D*/ { 1, 7, 0, 0, 0}, 
    /*E*/ { 3, 0, 0, 0, 0}}; 
  }

  }

  private int hash(Object key, int tableSize) {
    return key.hashCode() % tableSize;
  }

}