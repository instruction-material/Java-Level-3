package notices;
import java.util.List;
public final class OriginalOrder implements Ordering {
    public List<Notice> arrange(List<Notice> notices) { return List.copyOf(notices); }
}
