package creation;
import java.util.Objects;

public final class FamilyRenderer implements Renderer {
    private final Heading heading;
    private final BodyLine bodyLine;
    private final Style style;
    public FamilyRenderer(Heading heading, BodyLine bodyLine) {
        this.heading = Objects.requireNonNull(heading, "heading");
        this.bodyLine = Objects.requireNonNull(bodyLine, "bodyLine");
        style = Objects.requireNonNull(heading.style(), "heading style");
        if (style != Objects.requireNonNull(bodyLine.style(), "body style"))
            throw new IllegalArgumentException("product families must match");
    }
    public Style style() { return style; }
    public String render(Report report) {
        Objects.requireNonNull(report, "report");
        StringBuilder text = new StringBuilder(Objects.requireNonNull(heading.render(report.title()), "heading text"));
        for (String line : report.lines()) text.append(Objects.requireNonNull(bodyLine.render(line), "body text"));
        return text.toString();
    }
}
