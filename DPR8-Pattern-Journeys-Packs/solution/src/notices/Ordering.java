package notices;
import java.util.List;
/** Interchangeable ordering algorithm. Values and the supplied list stay unchanged. */
public interface Ordering { List<Notice> arrange(List<Notice> notices); }
