class Main {
    public static void main(String[] args) {
        Deque dq = new Deque(5);

        System.out.println();
        dq.insertBack(5);
        dq.insertBack(10);
        dq.insertBack(7);
        dq.insertBack(8);
        dq.insertBack(12);
        System.out.println(dq);
        System.out.println("get Front element: " + dq.getFront());
        System.out.println("get Back element: " + dq.getBack());

        System.out.println("\nremove the back");
        dq.removeBack();
        System.out.println(dq);

        System.out.println("the new back element is: " + dq.getBack());

        System.out.println("\ninsert 15 in the front");
        dq.insertFront(15);
        System.out.println(dq);
        System.out.println("get Front element: " + dq.getFront());

        System.out.println("\nremove the front");
        dq.removeFront();
        System.out.println(dq);
        System.out.println("the new front element is: " + dq.getFront());
    }
}
