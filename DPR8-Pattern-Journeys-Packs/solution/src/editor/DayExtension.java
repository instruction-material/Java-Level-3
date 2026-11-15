package editor;
import java.time.LocalDate;
import java.util.Objects;
/** Local Extension: a wrapper adds operations while the supplied API stays intact. */
public final class DayExtension {
    private final AncientDay date;
    public DayExtension(AncientDay date) { this.date = Objects.requireNonNull(date, "date"); }
    public int y() { return date.y(); }
    public int m() { return date.m(); }
    public int d() { return date.d(); }
    public String iso() { return LocalDate.of(date.y(), date.m(), date.d()).toString(); }
    public boolean weekend() { return LocalDate.of(date.y(), date.m(), date.d()).getDayOfWeek().getValue() >= 6; }
}
