package report.adapters;
import report.domain.Report;
import report.ports.ReportFormatter;
public final class TextFormatter implements ReportFormatter {
    public String format(Report report) {
        throw new UnsupportedOperationException("Implement the LF-delimited title and body format.");
    }
}
