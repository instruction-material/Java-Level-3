package report.adapters;
import java.util.Locale;
import java.util.Objects;
import report.domain.Report;
import report.ports.ReportFormatter;

/** Optional extension: only the title changes; body and LF contract stay fixed. */
public final class UppercaseTitleFormatter implements ReportFormatter {
    @Override
    public String format(Report report) {
        Objects.requireNonNull(report, "report");
        Report changed = new Report(report.title().toUpperCase(Locale.ROOT), report.lines());
        return new TextFormatter().format(changed);
    }
}
