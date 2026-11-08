package dispatch.ports;
import dispatch.domain.Dispatch;
import java.util.List;
import java.util.Optional;
/** Unique-id insertion and immutable insertion-order snapshots. Single-threaded. */
public interface DispatchRepository {
    Optional<Dispatch> find(String id);
    List<Dispatch> all();
    void insert(Dispatch dispatch);
}
