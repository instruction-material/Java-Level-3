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
    //TOSTRING METHODS ------------------------------------------------

    @Override
    public String toString() {
        return inOrder(this.root);
    }
    public String inOrder() {
        return inOrder(this.root);
    }
    private String inOrder(Node node) {

        String string = "";

        //base case
        if (node == null) {
            return string;
        }

        //add the left subtree values
        string += inOrder(node.left);
        //add in the MIDDLE of children
        string += node.value + " ";
        //add the right subtree values
        string += inOrder(node.right);

        return string;
    }
    public String preOrder() {
        return preOrder(this.root);
    }
    private String preOrder(Node node) {
        String string = "";

        //base case
        if (node == null) {
            return string;
        }

        //add BEFORE children
        string += node.value + " ";
        //add the left subtree values
        string += preOrder(node.left);
        //add the right subtree values
        string += preOrder(node.right);

        return string;
    }
    public String postOrder() {
        return postOrder(this.root);
    }
    public String postOrder(Node node) {
        String string = "";

        //base case
        if (node == null) {
            return string;
        }

        //add the left subtree values
        string += postOrder(node.left);
        //add the right subtree values
        string += postOrder(node.right);
        //add AFTER children
        string += node.value + " ";

        return string;
    }
}
