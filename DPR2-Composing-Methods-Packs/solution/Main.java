import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import reports.ComposedReport;
import reports.LegacyReport;
import reports.Summary;
import reports.comparison.MethodObjectReport;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        int[] samples = {12, -5, 20};
        int[] cursor = {0};
        List<String> events = new ArrayList<>();
        IntSupplier next = () -> samples[cursor[0]++];
        String mode = args.length == 0 ? "core" : args[0];
        Summary result;
        if (mode.equals("base")) result = LegacyReport.summarize(3, true, next, events::add);
        else if (mode.equals("object")) result = MethodObjectReport.summarize(3, true, next, events::add);
        else if (mode.equals("core")) result = ComposedReport.summarize(3, true, next, events::add);
        else throw new IllegalArgumentException("mode");
        System.out.println(result.format());
        System.out.println(cursor[0]);
        System.out.println(events);
    }
}
