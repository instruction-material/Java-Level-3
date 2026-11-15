package notices;
import java.util.List;
/** This public client API stays unchanged. */
public final class NoticeService implements NoticeAPI {
    public String render(String style, List<Notice> notices, boolean reverse) {
        NoticeCreator creator = NoticeCreator.forStyle(style);
        return creator.render(notices, reverse ? new ReversedOrder() : new OriginalOrder());
    }
}
