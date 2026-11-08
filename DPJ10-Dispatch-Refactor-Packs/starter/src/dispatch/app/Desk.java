package dispatch.app;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.ports.DeskApi;
import dispatch.ports.NoticeSink;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/** Initial runnable target: rendering, rates, storage and orchestration are coupled. */
public final class Desk implements DeskApi {
    private final LinkedHashMap<String,Dispatch> sent=new LinkedHashMap<>();
    private final NoticeSink sink;
    private boolean busy;
    public Desk(NoticeSink sink) { this.sink=Objects.requireNonNull(sink,"sink"); }
    public Dispatch preview(Request request) {
        Objects.requireNonNull(request,"request");
        String rendered;
        int unit;
        if(request.channel()==Request.Channel.EMAIL) {
            rendered="EMAIL: "+request.text();
            unit=100;
        } else {
            rendered="SMS: "+request.text();
            unit=60;
        }
        if(request.priority()==Request.Priority.URGENT) {
            rendered="URGENT "+rendered;
            unit+=40;
        }
        return new Dispatch(request.id(),rendered,unit*request.copies());
    }
    public Dispatch send(Request request) {
        if(busy)throw new IllegalStateException("reentrant send");
        busy=true;
        try {
            Objects.requireNonNull(request,"request");
            if(sent.containsKey(request.id()))throw new IllegalArgumentException("duplicate id");
            String rendered;
            int unit;
            if(request.channel()==Request.Channel.EMAIL) {
                rendered="EMAIL: "+request.text();
                unit=100;
            } else {
                rendered="SMS: "+request.text();
                unit=60;
            }
            if(request.priority()==Request.Priority.URGENT) {
                rendered="URGENT "+rendered;
                unit+=40;
            }
            Dispatch result=new Dispatch(request.id(),rendered,unit*request.copies());
            sent.put(result.id(),result);
            sink.sent(result);
            return result;
        } finally { busy=false; }
    }
    public Optional<Dispatch> find(String id) { return Optional.ofNullable(sent.get(Request.key(id))); }
    public List<Dispatch> all() { return List.copyOf(sent.values()); }
}
