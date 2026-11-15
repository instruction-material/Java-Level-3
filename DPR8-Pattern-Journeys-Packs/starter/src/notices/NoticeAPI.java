package notices;
import java.util.List;
public interface NoticeAPI {
    String render(String style, List<Notice> notices, boolean reverse);
}
