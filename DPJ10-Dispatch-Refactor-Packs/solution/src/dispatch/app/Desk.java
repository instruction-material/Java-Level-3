package dispatch.app;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.ports.DeskApi;
import dispatch.ports.DispatchRepository;
import dispatch.ports.NoticeSink;
import dispatch.ports.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/** The service owns send ordering and the guard; adapters own rendering and storage. */
public final class Desk implements DeskApi {
    private final DispatchRepository repository;
    private final Renderer renderer;
    private final NoticeSink sink;
    private boolean busy;
    public Desk(DispatchRepository repository,Renderer renderer,NoticeSink sink) {
        this.repository=Objects.requireNonNull(repository,"repository");
        this.renderer=Objects.requireNonNull(renderer,"renderer");
        this.sink=Objects.requireNonNull(sink,"sink");
    }
    public Dispatch preview(Request request) {
        Objects.requireNonNull(request,"request");
        Dispatch result=Objects.requireNonNull(renderer.render(request),"rendered dispatch");
        if(!result.id().equals(request.id()))throw new IllegalArgumentException("renderer changed id");
        return result;
    }
    public Dispatch send(Request request) {
        if(busy)throw new IllegalStateException("reentrant send");
        busy=true;
        try {
            Objects.requireNonNull(request,"request");
            if(repository.find(request.id()).isPresent())throw new IllegalArgumentException("duplicate id");
            Dispatch result=preview(request);
            repository.insert(result);
            sink.sent(result);
            return result;
        } finally { busy=false; }
    }
    public Optional<Dispatch> find(String id) { return repository.find(Request.key(id)); }
    public List<Dispatch> all() { return List.copyOf(repository.all()); }
    public List<Dispatch> sendBatch(List<Request> requests) {
        if(busy)throw new IllegalStateException("reentrant send");
        List<Request> selected=List.copyOf(Objects.requireNonNull(requests,"requests"));
        List<Dispatch> result=new ArrayList<>();
        for(Request request:selected)result.add(send(request));
        return List.copyOf(result);
    }
}
