class Main {
    public static void main(String[] args) {
        DoublyLinkedList<Integer> list1 = new DoublyLinkedList<>();
        DoublyLinkedList<Integer> list2 = new DoublyLinkedList<>();

        for (int i = 1; i <= 10; i++) {
            list1.insertBack(i);
            list2.insertFront(i);
        }

        System.out.println("\nList 1: " + list1);
        System.out.println("List 2: " + list2);
        System.out.println("List 1 Length: " + list1.getLength());
        System.out.println("List 2 Length: " + list2.getLength());

        System.out.println("\nRemove the back of List 1");
        list1.removeBack();
        System.out.println("List 1: " + list1);
        System.out.println("List 1 Length: " + list1.getLength());

        System.out.println("\nRemove the front of List 2");
        list2.removeFront();
        System.out.println("List 2: " + list2);
        System.out.println("List 2 Length: " + list2.getLength());

        System.out.println("\nClear List 1");
        list1.clearAll();
        System.out.println("List 1: " + list1);
        System.out.println("List 1 Length: " + list1.getLength());
    }
}
