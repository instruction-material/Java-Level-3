public class Main {
    public static void main(String[] args) {
        var table = new HashMap<Integer, String>();
        table.insert(46, "first");
        table.insert(93, "second");
        table.remove(46);
        table.put(93, "updated");
        System.out.println(table.get(93));
        System.out.println(table.has(46));
        if (args.length == 1 && args[0].equals("compact")) {
            table.compact();
            System.out.println("Deleted slots: " + table.deletedSlots());
        }
        System.out.println(table.size());
    }
}
