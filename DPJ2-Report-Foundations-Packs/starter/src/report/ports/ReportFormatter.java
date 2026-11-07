package report.ports;
import report.domain.Report;
@FunctionalInterface
public interface ReportFormatter {
    /** Return non-null text for a non-null report; keep the report unchanged. */
    String format(Report report);
}
