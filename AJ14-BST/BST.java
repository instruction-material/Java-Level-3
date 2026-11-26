import java.util.StringJoiner;

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
        this.root = deleteAll(this.root);
    }
    private Node deleteAll(Node node) {

        //base case
        if (node == null) {
            return null;
        }

        //delete left-subtree
        node.left = deleteAll(node.left);
        //delete right-subtree
        node.right = deleteAll(node.right);
        //delete the root
        size--;
        return null;
    }

    //REMOVE METHODS --------------------------------------------------
    @Override
    public void remove(int value) {
        this.root = delete(this.root, value);
        size--;
    }
    private Node delete(Node node, int value) {

        //base case
        if (node == null) {
            //error: deletion value isn't in tree
            throw new IllegalArgumentException(value +
                                               "doesn't exist in the tree");
        }
        if (node.value == value) {
            //remove the node (by returning the replacement)
            return getReplacement(node);
        }

        //recursive case
        if (value < node.value) {
            node.left = delete(node.left, value);
        } else {
            node.right = delete(node.right, value);
        }
        return node;
    }
    private Node getReplacement(Node deletedNode) {

        //0 children
        if (deletedNode.left == null && deletedNode.right == null) {
            return null;
        }

        //1 child
        if (deletedNode.left == null) {
            return deletedNode.right;
        }
        if (deletedNode.right == null) {
            return deletedNode.left;
        }

        //2 children
        //find and delete the inorder predecessor
        int predecessorValue = inOrderPredecessor(deletedNode).value;
        deletedNode.left = delete(deletedNode.left, predecessorValue);
        //create a replacement with the value of the inorder predecessor
        Node replacement = new Node(predecessorValue);
        replacement.left = deletedNode.left;
        replacement.right = deletedNode.right;

        return replacement;
    }
    private Node inOrderPredecessor(Node node) {
        //the "in-order predecessor":
        //the left child's right-most child
        //(or just the left child if it has no children)
        Node predecessor = node.left;
        while (predecessor.right != null) {
            predecessor = predecessor.right;
        }
        return predecessor;
    }

    //TOSTRING METHODS ------------------------------------------------

    @Override
    public String toString() {
        return inOrder();
    }

    public String inOrder() {
        var values = new StringJoiner(" ");
        inOrder(root, values);
        return values.toString();
    }

    private void inOrder(Node node, StringJoiner values) {
        if (node == null) return;
        inOrder(node.left, values);
        values.add(Integer.toString(node.value));
        inOrder(node.right, values);
    }

    public String preOrder() {
        var values = new StringJoiner(" ");
        preOrder(root, values);
        return values.toString();
    }

    private void preOrder(Node node, StringJoiner values) {
        if (node == null) return;
        values.add(Integer.toString(node.value));
        preOrder(node.left, values);
        preOrder(node.right, values);
    }

    public String postOrder() {
        return postOrder(root);
    }

    public String postOrder(Node node) {
        var values = new StringJoiner(" ");
        postOrder(node, values);
        return values.toString();
    }

    private void postOrder(Node node, StringJoiner values) {
        if (node == null) return;
        postOrder(node.left, values);
        postOrder(node.right, values);
        values.add(Integer.toString(node.value));
    }
}
