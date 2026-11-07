package report.adapters;
import report.domain.Report;
import report.ports.ReportFormatter;
/** Optional task, reached only by the complete harness. */
public final class UppercaseTitleFormatter implements ReportFormatter {
    public String format(Report report) {
        throw new UnsupportedOperationException("Implement the locale-independent uppercase-title extension.");
    }
}
