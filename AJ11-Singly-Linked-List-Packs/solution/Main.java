public class Main {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.insertBack("B");
        list.insertFront("A");
        list.insertBack("C");
        if (args.length == 1 && args[0].equals("find")) {
            System.out.println(list.firstIndex("B"));
            return;
        }
        System.out.println(list);
        list.removeFront();
        list.removeBack();
        System.out.println(list.get(0));
        list.clearAll();
        System.out.println(list.getLength());
    }
}
