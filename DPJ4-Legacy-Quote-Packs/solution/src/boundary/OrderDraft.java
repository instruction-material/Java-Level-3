// Prototype: editable draft copies own both their list and mutable lines.
package boundary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class OrderDraft {
    private final String title;
    private final List<Line> lines = new ArrayList<>();
    public static final class Line {
        private final String name;
        private int units;
        public Line(String name, int units) { this.name = text(name); setUnits(units); }
        public String name() { return name; }
        public int units() { return units; }
        public void setUnits(int value) {
            if (value < 1 || value > 1000) throw new IllegalArgumentException("line units must be 1..1000");
            units = value;
        }
    }
    private static String text(String value) {
        Objects.requireNonNull(value, "text");
        if (value.isBlank() || value.contains("\n") || value.contains("\r"))
            throw new IllegalArgumentException("text must be a nonblank single line");
        return value;
    }
    public OrderDraft(String title, List<Line> source) {
        this.title = text(title);
        Objects.requireNonNull(source, "lines");
        for (Line line : source) {
            Objects.requireNonNull(line, "line");
            lines.add(new Line(line.name(), line.units()));
        }
    }
    public String title() { return title; }
    /** The list structure is read-only, but its Line elements remain editable. */
    public List<Line> lines() { return Collections.unmodifiableList(lines); }
    public void add(String name, int units) { lines.add(new Line(name, units)); }
    public int totalUnits() {
        int total = 0;
        for (Line line : lines) total = Math.addExact(total, line.units());
        return total;
    }
    public OrderDraft copy() { return new OrderDraft(title, lines); }
    public OrderDraft copyWithTitle(String value) { return new OrderDraft(value, lines); }
}
