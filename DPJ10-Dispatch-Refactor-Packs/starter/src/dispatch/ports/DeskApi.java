package dispatch.ports;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import java.util.List;
import java.util.Optional;
/** Public behavior stays stable while the internal collaborators change. */
public interface DeskApi {
    Dispatch preview(Request request);
    Dispatch send(Request request);
    Optional<Dispatch> find(String id);
    List<Dispatch> all();
    default List<Dispatch> sendBatch(List<Request> requests) {
        throw new UnsupportedOperationException("Implement optional prefix-commit batch.");
    }
}
