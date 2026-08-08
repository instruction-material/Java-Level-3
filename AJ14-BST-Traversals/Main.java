class Main {
    public static void main(String[] args) {
        BST tree = new BST();

        //add values to the BST
        System.out.println();
        tree.insert(6);
        System.out.println(tree);
        tree.insert(8);
        System.out.println(tree);
        tree.insert(9);
        System.out.println(tree);
        tree.insert(7);
        System.out.println(tree);
        tree.insert(3);
        System.out.println(tree);
        tree.insert(2);
        System.out.println(tree);
        tree.insert(5);
        System.out.println(tree);

        System.out.println("\nInorder:   " + tree.inOrder());
        System.out.println("Preorder:  " + tree.preOrder());
        System.out.println("Postorder: " + tree.postOrder());
    }
}
