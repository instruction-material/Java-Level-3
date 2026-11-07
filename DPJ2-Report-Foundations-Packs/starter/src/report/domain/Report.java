package report.domain;
import java.util.List;
public record Report(String title, List<String> lines) {
    public Report(String title, List<String> lines) {
        this.title = title;
        this.lines = lines;
        throw new UnsupportedOperationException("Implement Report validation and immutable snapshot.");
    }
}
