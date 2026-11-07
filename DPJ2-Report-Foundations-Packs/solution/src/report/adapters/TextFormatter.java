package report.adapters;
import java.util.Objects;
import report.domain.Report;
import report.ports.ReportFormatter;

public final class TextFormatter implements ReportFormatter {
    @Override
    public String format(Report report) {
        Objects.requireNonNull(report, "report");
        StringBuilder text = new StringBuilder(report.title()).append('\n');
        for (String line : report.lines()) text.append(line).append('\n');
        return text.toString();
    }
}
