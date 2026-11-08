package dates;

import java.time.LocalDate;
import java.util.Objects;

/** A complete foreign-method comparison using an immutable JDK type. */
public final class ForeignDateMethods {
    private ForeignDateMethods() {}
    public static LocalDate followingMonday(LocalDate date) {
        Objects.requireNonNull(date, "date");
        int forward = 8 - date.getDayOfWeek().getValue();
        return date.plusDays(forward);
    }
}
