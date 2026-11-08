package dates;

import java.time.LocalDate;
import java.util.Objects;

/** A complete composition-based local extension, compared with one static helper. */
public record BusinessDate(LocalDate date) {
    public BusinessDate { Objects.requireNonNull(date, "date"); }
    public BusinessDate followingMonday() {
        return new BusinessDate(ForeignDateMethods.followingMonday(date));
    }
}
