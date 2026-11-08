package dispatch.infra;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.ports.Renderer;
import java.util.Objects;
/** A closed pair of channels stays an explicit switch instead of a speculative factory hierarchy. */
public final class StandardRenderer implements Renderer {
    public Dispatch render(Request request) {
        Objects.requireNonNull(request,"request");
        String prefix=switch(request.channel()) { case EMAIL -> "EMAIL: "; case SMS -> "SMS: "; };
        int unit=switch(request.channel()) { case EMAIL -> 100; case SMS -> 60; };
        if(request.priority()==Request.Priority.URGENT) { prefix="URGENT "+prefix;unit+=40; }
        return new Dispatch(request.id(),prefix+request.text(),unit*request.copies());
    }
}
