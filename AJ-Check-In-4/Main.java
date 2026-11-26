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
    -Height: The largest number of edges from the root to a leaf
    -Subtree: A tree within a tree, where one of the nodes is the new root

    // BST-3: A is the root, with children B and E. B has children C and D. E has children F and G.
    // Trace pre-order, in-order, and post-order traversals.

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

    Divide the number of entries by the capacity. Rehash when the load factor exceeds 0.5.
    695 / 1,543 is approximately 0.450.
    420,000 / 700,001 is approximately 0.600. Rehash.
    98 / 193 is approximately 0.508. Rehash.

    */

        ////////////////////////////////
        //// GRAPHS
        ////////////////////////////////

        /*
    Graph-2: Define the following terms:
    -Vertex: Node in a graph
    -Edge: Connects vertices
    -Circuit: A walk that returns to its starting vertex without reusing an edge
    -Directed: Each edge is an ordered pair from its source to its destination
    -Undirected: An edge can be traversed in either direction
    -Path: An ordering of vertices that can be traversed from one to the next
    -Adjacent Vertices: Vertices directly connected by an edge
    -Degree: Count incident edges in an undirected graph, and count incoming and outgoing edges separately in a directed graph
    -Weighted: Weights on the edges
    */

        // Graph-3: Read each row as a source followed by its outgoing neighbors.
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

        // Graph-4: The undirected graph below uses positive weights. Zero marks a missing edge.
        int[][] matrix = new int[][] {/*
            A  B  C  D  E
    /*A*/
                                      {0, 1, 1, 1, 3},
                                      /*B*/ {1, 0, 5, 7, 0},
                                      /*C*/ {1, 5, 0, 0, 0},
                                      /*D*/ {1, 7, 0, 0, 0},
                                      /*E*/ {3, 0, 0, 0, 0}};
    }

    // Compute the table index for the provided key
    private int hash(Object key, int tableSize) {
        return Math.floorMod(key.hashCode(), tableSize);
    }
}
