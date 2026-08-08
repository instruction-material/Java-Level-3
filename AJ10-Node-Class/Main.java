class Main {
    public static void main(String[] args) {

        //Integer test
        Node<Integer> test1 = new Node<>(2);
        test1.set(3);
        System.out.println("\n" + test1);

        //String test
        Node<String> test2 = new Node<>("slam");
        test2.set(test2.get() + " dunk");
        System.out.println(test2);
    }
}
