import api.*;
import app.*;
import legacy.LegacyWorkshopDesk;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
public final class Main {
    private Main() {}
    public static void main(String[] args) throws Exception {
        String mode = args.length == 0 ? "core" : args[0];
        DeskAPI desk = switch (mode) {
            case "baseline", "script-baseline" -> new LegacyWorkshopDesk();
            case "core", "script-core" -> new WorkshopDesk();
            default -> throw new IllegalArgumentException("mode");
        };
        if (mode.startsWith("script-")) {
            var input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            System.out.print(AppScript.run(desk, input.lines().toList()));
        } else {
            desk.open("room", 2);
            desk.open("quiet", 1);
            desk.join("room", "ana");
            desk.join("room", "bo");
            desk.join("room", "cy");
            desk.leave("room", "ana");
            desk.close("quiet");
            System.out.print(desk.report("plain"));
            System.out.print(desk.report("compact"));
            System.out.println(desk.view("room").history());
        }
    }
}
