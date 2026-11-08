package dispatch;
import dispatch.app.Desk;
import dispatch.infra.MemoryDispatches;
import dispatch.infra.StandardRenderer;
import dispatch.ports.DeskApi;
import dispatch.ports.NoticeSink;
/** The composition root chooses concrete adapters. */
public final class Composition {
    private Composition() {}
    public static DeskApi create(NoticeSink sink) {
        return new Desk(new MemoryDispatches(),new StandardRenderer(),sink);
    }
}
