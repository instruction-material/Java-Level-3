class Main {
    public static void main(String[] args) {
        BST tree = new BST();

        //add values to the BST
        tree.insert(6);
        tree.insert(8);
        tree.insert(9);
        tree.insert(7);
        tree.insert(3);
        tree.insert(2);
        tree.insert(5);

        //size
        System.out.println("\nSize: " + tree.size());

        //true
        System.out.println("Is 3 in the tree? " + tree.has(3));
        System.out.println("Is 5 in the tree? " + tree.has(5));

        //false
        System.out.println("Is 12 in the tree? " + tree.has(12));
        System.out.println("Is 1 in the tree? " + tree.has(1));
    }
}
