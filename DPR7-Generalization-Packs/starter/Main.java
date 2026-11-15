import roster.*;
import reports.*;
import events.*;
import java.util.List;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode = args.length == 0 ? "baseline" : args[0];
        if (mode.equals("baseline") || mode.equals("core")) {
            System.out.print(CompatibilityClient.run(mode.equals("baseline") ? new LegacyRoster() : new Roster()));
            System.out.println(new SponsorBanner("North Lab").line());
        } else if (mode.equals("transfer-baseline") || mode.equals("transfer")) {
            List<Row> rows = List.of(new Row("tool, kit", 2), new Row("lamp", 3));
            boolean before = mode.equals("transfer-baseline");
            System.out.print(before ? LegacyReports.render(Format.PLAIN, rows) : ReportService.render(Format.PLAIN, rows));
            System.out.println("---");
            System.out.print(before ? LegacyReports.render(Format.CSV, rows) : ReportService.render(Format.CSV, rows));
        } else if (mode.equals("extension-baseline") || mode.equals("extension")) {
            boolean before = mode.equals("extension-baseline");
            EventBufferAPI buffer = before ? LegacyEventModels.buffer(2) : EventFactory.create(2);
            buffer.push("A"); buffer.push("B"); buffer.push("C");
            System.out.println(buffer.snapshot());
            System.out.println(buffer.next());
            System.out.println(buffer.snapshot());
            NamedValue counter = before ? LegacyEventModels.named("visits") : new NamedCounter("visits");
            counter.add(5); counter.add(2);
            System.out.println(counter.label() + "=" + counter.value());
            counter.reset();
            System.out.println("reset=" + counter.value());
        } else throw new IllegalArgumentException("mode");
    }
}
