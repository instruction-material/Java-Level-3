package notices;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/** Frozen working before design: format conditionals and traversal are coupled. */
public final class LegacyNotices implements NoticeAPI {
    public String render(String style, List<Notice> notices, boolean reverse) {
        Objects.requireNonNull(style, "style");
        if (!style.equals("text") && !style.equals("bracket"))
            throw new IllegalArgumentException("style");
        Objects.requireNonNull(notices, "notices");
        List<Notice> rows = new ArrayList<>();
        for (Notice row : notices) rows.add(Objects.requireNonNull(row, "notice"));
        if (reverse) Collections.reverse(rows);
        StringBuilder result = new StringBuilder("Notes\n");
        for (Notice row : rows) {
            if (style.equals("text")) result.append(row.label()).append('=').append(row.count()).append('\n');
            else result.append('[').append(row.label()).append(':').append(row.count()).append("]\n");
        }
        return result.append("end=").append(rows.size()).append('\n').toString();
    }
}
