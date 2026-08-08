public class BST implements BSTInterface {

    //INSTANCE VARIABLES
    private Node root;
    private int size;

    //NODE INNER CLASS ------------------------------------------------
    private class Node {

        int value;
        Node left = null;
        Node right = null;

        public Node(int value) {
            this.value = value;
        }
    }

    //METHODS ---------------------------------------------------------
    @Override
    public int size() {
        return this.size;
    }
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    //INSERT METHODS --------------------------------------------------
    @Override
    public void insert(int value) {
        this.root = add(this.root, value);
        size++;
    }
    private Node add(Node node, int value) {

        //base case
        if (node == null) {
            //add a new node
            return new Node(value);
        }
        if (node.value == value) {
            //error: insertion value is already in tree
            throw new IllegalArgumentException(
                value +
                "already exists in the tree. No duplicates are allowed.");
        }

        //recursive case
        if (value < node.value) {
            node.left = add(node.left, value);
        } else {
            node.right = add(node.right, value);
        }
        return node;
    }

    //FIND METHODS ----------------------------------------------------
    @Override
    public boolean has(int value) {
        return find(this.root, value) != null;
    }
    private Node find(Node node, int value) {

        //base case
        if (node == null) {
            return null;
        }
        if (node.value == value) {
            return node;
        }

        //recursive case
        if (value < node.value) {
            return find(node.left, value);
        } else {
            return find(node.right, value);
        }
    }

    //CLEAR METHODS ---------------------------------------------------
    @Override
    public void clear() {
        //TODO
    }

    //REMOVE METHODS --------------------------------------------------
    @Override
    public void remove(int value) {
        //TODO
    }
}
