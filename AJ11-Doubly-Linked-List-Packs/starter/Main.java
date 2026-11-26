public class Main {
    public static void main(String[] args) {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        list.insertBack(12);
        list.insertFront(7);
        list.insertBack(21);
        System.out.println(list);
        System.out.println(list.reverseValues());
        list.removeFront();
        list.removeBack();
        System.out.println(list.get(0));
    }
}
