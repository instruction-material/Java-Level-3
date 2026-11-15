package notices;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class ReversedOrder implements Ordering {
    public List<Notice> arrange(List<Notice> notices) {
        List<Notice> copy = new ArrayList<>(notices);
        Collections.reverse(copy);
        return List.copyOf(copy);
    }
}
