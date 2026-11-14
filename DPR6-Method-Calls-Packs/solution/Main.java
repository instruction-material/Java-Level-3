import dispatch.*;

public final class Main {
    private Main() {}
    private static String baseline() {
        LegacyDesk desk = new LegacyDesk(0);
        StringBuilder output = new StringBuilder();
        output.append(desk.price("A", 500, 1, false, null)).append("\n");
        desk.setDiscount(25);
        output.append(desk.price("B", 501, 2, true, "ignored")).append("\n");
        output.append(desk.totalCents()).append("\n");
        output.append(desk.price(null, 0, 0, false, null)).append("\n");
        output.append(desk.find("B")).append("\n");
        output.append(desk.find("missing"));
        return output.toString();
    }
    public static void main(String[] args) {
        if (args.length != 1 || !(args[0].equals("baseline") || args[0].equals("core")))
            throw new IllegalArgumentException("mode");
        System.out.println(args[0].equals("baseline") ? baseline() : ClientCompatibility.run());
    }
}
