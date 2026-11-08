package dispatch.ports;
import dispatch.domain.Dispatch;
@FunctionalInterface
public interface NoticeSink {
    void sent(Dispatch dispatch);
}
