package notices;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public abstract class NoticeCreator {
    /** A static selection factory; subclass overriding happens in createLine(). */
    public static NoticeCreator forStyle(String style) {
        Objects.requireNonNull(style, "style");
        return switch (style) {
            case "text" -> new TextCreator();
            case "bracket" -> new BracketCreator();
            default -> throw new IllegalArgumentException("style");
        };
    }
    /** Template Method: validation, ordering, header, products, footer. */
    public final String render(List<Notice> notices, Ordering ordering) {
        Objects.requireNonNull(notices, "notices");
        List<Notice> snapshot = new ArrayList<>();
        for (Notice row : notices) snapshot.add(Objects.requireNonNull(row, "notice"));
        List<Notice> rows = Objects.requireNonNull(ordering, "ordering").arrange(List.copyOf(snapshot));
        StringBuilder result = new StringBuilder("Notes\n");
        NoticeLine line = createLine();
        for (Notice row : rows) result.append(line.format(row));
        return result.append("end=").append(rows.size()).append('\n').toString();
    }
    /** Factory Method: creator subclasses supply a common product. */
    protected abstract NoticeLine createLine();
}
