class Main {
    public static void main(String[] args) {
        BST tree = new BST();

        System.out.println("\nDepth First vs. Breadth First");

        //add values to the BST
        tree.insert(6);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(8);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(9);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(7);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(3);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(2);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.insert(5);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.remove(6);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());

        tree.remove(9);
        System.out.println("\nDF:" + tree.inOrder());
        System.out.println("BF:" + tree.BFS());
    }
}
