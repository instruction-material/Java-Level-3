import wrapping.ReadDesk;
import wrapping.Source;
import wrapping.legacy.LegacyReader;

public final class Main {
    public static void main(String[] args) {
        int[] loads = {0};
        Source source = key -> { loads[0]++; return "Hi"; };
        if (args.length > 0 && args[0].equals("base")) {
            LegacyReader desk = new LegacyReader(source, "tag:", true, true);
            System.out.println(desk.read("a"));
            System.out.println(desk.read("a"));
            System.out.println(desk.attempts());
            System.out.println(loads[0]);
            desk.clearCache();
            System.out.println(desk.read("a"));
            System.out.println(loads[0]);
        } else {
            ReadDesk desk = new ReadDesk(source, "tag:", true, true);
            System.out.println(desk.read("a"));
            System.out.println(desk.read("a"));
            System.out.println(desk.attempts());
            System.out.println(loads[0]);
            desk.clearCache();
            System.out.println(desk.read("a"));
            System.out.println(loads[0]);
        }
    }
}
