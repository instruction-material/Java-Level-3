import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import report.adapters.TextFormatter;
import report.adapters.UppercaseTitleFormatter;
import report.application.ReportPublisher;
import report.domain.Report;
import report.examples.LanguageChoices;
import report.legacy.ReportBlob;
import report.ports.ReportFormatter;
import report.ports.ReportSink;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        if (!actual.equals(expected)) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void yes(boolean value) {
        if (!value) throw new AssertionError("expected true");
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Wrong failure", error);
            checks++; return;
        }
        throw new AssertionError("Missing " + type.getSimpleName());
    }
    private static final class Recorder implements ReportSink {
        int calls; String text = "";
        public void write(String value) { calls++; text = value; }
    }
    private static void core() {
        for (int count = 0; count <= 100; count++) {
            List<String> input = new ArrayList<>();
            String expected = "Title " + count + "\n";
            for (int i = 0; i < count; i++) {
                input.add(i % 3 == 0 ? "" : "line " + i);
                expected += input.get(i) + "\n";
            }
            Report report = new Report("Title " + count, input);
            input.add("caller changed");
            equal(report.lines().size(), count);
            equal(new TextFormatter().format(report), expected);
            equal(ReportBlob.render(report.title(), report.lines()), expected);
            Recorder recorder = new Recorder();
            new ReportPublisher(new TextFormatter(), recorder).publish(report);
            equal(recorder.calls, 1);
            equal(recorder.text, expected);
            fails(UnsupportedOperationException.class, () -> report.lines().add("change"));
        }
        Report report = new Report("i", List.of("Body", ""));
        equal(report, new Report("i", List.of("Body", "")));
        equal(report.hashCode(), new Report("i", List.of("Body", "")).hashCode());
        equal(report.title(), "i");
        int[] calls = {0};
        Recorder recorder = new Recorder();
        ReportFormatter fake = value -> {
            yes(value == report); calls[0]++; return "fake\n";
        };
        new ReportPublisher(fake, recorder).publish(report);
        equal(calls[0], 1); equal(recorder.calls, 1); equal(recorder.text, "fake\n");
        ReportFormatter bad = value -> { throw new IllegalStateException("format failed"); };
        Recorder untouched = new Recorder();
        fails(IllegalStateException.class, () -> new ReportPublisher(bad, untouched).publish(report));
        equal(untouched.calls, 0);
        fails(NullPointerException.class, () -> new ReportPublisher(value -> null, untouched).publish(report));
        equal(untouched.calls, 0);
        fails(IllegalStateException.class, () -> new ReportPublisher(new TextFormatter(), value -> { throw new IllegalStateException("sink failed"); }).publish(report));
        fails(NullPointerException.class, () -> new ReportPublisher(null, recorder));
        fails(NullPointerException.class, () -> new ReportPublisher(new TextFormatter(), null));
        fails(NullPointerException.class, () -> new ReportPublisher(fake, recorder).publish(null));
        equal(calls[0], 1); equal(recorder.calls, 1);
        fails(NullPointerException.class, () -> new TextFormatter().format(null));
        fails(NullPointerException.class, () -> new Report(null, List.of()));
        fails(NullPointerException.class, () -> new Report("T", null));
        fails(NullPointerException.class, () -> new Report("T", Arrays.asList("ok", null)));
        for (String title : List.of("", " ", "\t", "a\nb", "a\rb"))
            fails(IllegalArgumentException.class, () -> new Report(title, List.of()));
        for (String line : List.of("a\nb", "a\rb"))
            fails(IllegalArgumentException.class, () -> new Report("T", List.of(line)));
        equal(new TextFormatter().format(new Report("T", List.of())), "T\n");
        equal(new TextFormatter().format(new Report("T", List.of(""))), "T\n\n");
        equal(LanguageChoices.lambdaExample(), "Weekly report\n");
        equal(LanguageChoices.preview(report, new TextFormatter(), LanguageChoices.PreviewMode.TITLE_ONLY), new LanguageChoices.Published("i\n"));
        equal(LanguageChoices.preview(report, new TextFormatter(), LanguageChoices.PreviewMode.FULL), new LanguageChoices.Published("i\nBody\n\n"));
        equal(LanguageChoices.preview(new Report("T", List.of()), bad, LanguageChoices.PreviewMode.FULL), new LanguageChoices.Refused("No body"));
        equal(LanguageChoices.describe(new LanguageChoices.Refused("No body")), "Refused: No body");
        equal(LanguageChoices.describe(new LanguageChoices.Published("OK")), "Published: OK");
        fails(NullPointerException.class, () -> LanguageChoices.preview(null, new TextFormatter(), LanguageChoices.PreviewMode.FULL));
        fails(NullPointerException.class, () -> LanguageChoices.preview(report, null, LanguageChoices.PreviewMode.FULL));
        fails(NullPointerException.class, () -> LanguageChoices.preview(report, new TextFormatter(), null));
        fails(NullPointerException.class, () -> LanguageChoices.preview(report, value -> null, LanguageChoices.PreviewMode.FULL));
        fails(NullPointerException.class, () -> LanguageChoices.describe(null));
        fails(NullPointerException.class, () -> new LanguageChoices.Published(null));
        fails(NullPointerException.class, () -> new LanguageChoices.Refused(null));
    }
    private static void extension() {
        equal(new UppercaseTitleFormatter().format(new Report("Weekly report", List.of("Ready", "Checked"))), "WEEKLY REPORT\nReady\nChecked\n");
        equal(new UppercaseTitleFormatter().format(new Report("i", List.of("body"))), "I\nbody\n");
        equal(new UppercaseTitleFormatter().format(new Report("Title", List.of())), "TITLE\n");
        fails(NullPointerException.class, () -> new UppercaseTitleFormatter().format(null));
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            equal(new UppercaseTitleFormatter().format(new Report("i", List.of("body"))), "I\nbody\n");
        } finally { Locale.setDefault(previous); }
    }
    public static void main(String[] args) {
        boolean coreOnly = args.length == 1 && args[0].equals("core");
        core();
        if (!coreOnly) extension();
        System.out.println((coreOnly ? "Core foundations contracts: " : "Complete foundations contracts: ") + checks + " passed");
    }
}
