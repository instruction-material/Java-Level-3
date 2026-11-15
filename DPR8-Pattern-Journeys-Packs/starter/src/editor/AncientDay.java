package editor;
import java.time.LocalDate;
/** Supplied external API. Keep this implementation unchanged. */
public final class AncientDay {
    private final LocalDate value;
    public AncientDay(int year, int month, int day) {
        if (year < 1000 || year > 9999) throw new IllegalArgumentException("year");
        value = LocalDate.of(year, month, day);
    }
    public int y() { return value.getYear(); }
    public int m() { return value.getMonthValue(); }
    public int d() { return value.getDayOfMonth(); }
}
