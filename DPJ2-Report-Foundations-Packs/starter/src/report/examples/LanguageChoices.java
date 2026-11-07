package report.examples;
import java.util.List;
import java.util.Objects;
import report.domain.Report;
import report.ports.ReportFormatter;

/** Worked language alternatives; the preview rule requires a nonempty body. */
public final class LanguageChoices {
    private LanguageChoices() {}
    public enum PreviewMode { TITLE_ONLY, FULL }
    public sealed interface Outcome permits Published, Refused {}
    public record Published(String text) implements Outcome {
        public Published { Objects.requireNonNull(text, "text"); }
    }
    public record Refused(String reason) implements Outcome {
        public Refused { Objects.requireNonNull(reason, "reason"); }
    }
    public static Outcome preview(Report report, ReportFormatter formatter, PreviewMode mode) {
        Objects.requireNonNull(report, "report");
        Objects.requireNonNull(formatter, "formatter");
        Objects.requireNonNull(mode, "mode");
        if (report.lines().isEmpty()) return new Refused("No body");
        return new Published(mode == PreviewMode.TITLE_ONLY ?
            report.title() + "\n" : Objects.requireNonNull(formatter.format(report), "text"));
    }
    public static String describe(Outcome result) {
        return switch (Objects.requireNonNull(result, "result")) {
            case Published value -> "Published: " + value.text();
            case Refused value -> "Refused: " + value.reason();
        };
    }
    public static String lambdaExample() {
        ReportFormatter heading = report -> report.title() + "\n";
        return heading.format(new Report("Weekly report", List.of("Ready")));
    }
}
