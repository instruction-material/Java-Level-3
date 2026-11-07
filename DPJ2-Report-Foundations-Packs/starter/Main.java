import java.util.List;
import report.adapters.ConsoleSink;
import report.adapters.TextFormatter;
import report.application.ReportPublisher;
import report.domain.Report;
public class Main {
    public static void main(String[] args) {
        Report report = new Report("Weekly report", List.of("Ready", "Checked"));
        new ReportPublisher(new TextFormatter(), new ConsoleSink()).publish(report);
    }
}
