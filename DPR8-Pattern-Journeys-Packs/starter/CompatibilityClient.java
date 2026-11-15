import notices.*;
import tickets.*;
import editor.*;
import java.util.List;
/** Frozen client compiles unchanged against starter and reference public APIs. */
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static String run(NoticeAPI n, TicketAPI t, EditorAPI e) {
        String notice = n.render("text", List.of(new Notice("lamp", 2)), false);
        t.apply("approve"); e.append(2026, 11, 15, "lamp");
        return notice + t.status() + "\n" + e.text();
    }
}
