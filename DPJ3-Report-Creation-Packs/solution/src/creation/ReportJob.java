// Builder: named options become an immutable job snapshot.
package creation;
import java.util.Objects;

public record ReportJob(Report report, Style style, boolean includeCount, String footer) {
    public ReportJob {
        Objects.requireNonNull(report, "report");
        Objects.requireNonNull(style, "style");
        footer = validFooter(footer);
    }
    private static String validFooter(String value) {
        Objects.requireNonNull(value, "footer");
        if (value.contains("\n") || value.contains("\r"))
            throw new IllegalArgumentException("footer must be one line");
        return value;
    }
    public String render() {
        String text = RendererFactory.forStyle(style).render(report);
        if (includeCount) text += "Lines: " + report.lines().size() + "\n";
        if (!footer.isEmpty()) text += footer + "\n";
        return text;
    }
    public Builder toBuilder() {
        return new Builder(report).style(style).includeCount(includeCount).footer(footer);
    }
    public static final class Builder {
        private final Report report;
        private Style style = Style.PLAIN;
        private boolean includeCount;
        private String footer = "";
        public Builder(Report report) { this.report = Objects.requireNonNull(report, "report"); }
        public Builder style(Style value) {
            style = Objects.requireNonNull(value, "style"); return this;
        }
        public Builder includeCount(boolean value) {
            includeCount = value; return this;
        }
        public Builder footer(String value) {
            footer = validFooter(value); return this;
        }
        public ReportJob build() { return new ReportJob(report, style, includeCount, footer); }
    }
}
