package dispatch.infra;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.ports.DispatchRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
public final class MemoryDispatches implements DispatchRepository {
    private final LinkedHashMap<String,Dispatch> sent=new LinkedHashMap<>();
    public Optional<Dispatch> find(String id) { return Optional.ofNullable(sent.get(Request.key(id))); }
    public List<Dispatch> all() { return List.copyOf(sent.values()); }
    public void insert(Dispatch dispatch) {
        Objects.requireNonNull(dispatch,"dispatch");
        if(sent.containsKey(dispatch.id()))throw new IllegalArgumentException("duplicate id");
        sent.put(dispatch.id(),dispatch);
    }
}
