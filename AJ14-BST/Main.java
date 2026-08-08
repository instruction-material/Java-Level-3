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
        tree.remove(6);
        System.out.println(tree);
        tree.remove(9);
        System.out.println(tree);

        //test the length and has method
        System.out.println("\nSize: " + tree.size());
        System.out.println("Is 3 in the tree? " + tree.has(3));

        //clear tree
        tree.clear();
        System.out.println("\nClear the tree:" + tree);
        System.out.println("Size: " + tree.size());
        System.out.println("Is 3 in the tree? " + tree.has(3));
    }
}
