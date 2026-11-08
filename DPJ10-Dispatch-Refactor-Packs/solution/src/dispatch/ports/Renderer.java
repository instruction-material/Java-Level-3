package dispatch.ports;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
@FunctionalInterface
public interface Renderer {
    Dispatch render(Request request);
}
