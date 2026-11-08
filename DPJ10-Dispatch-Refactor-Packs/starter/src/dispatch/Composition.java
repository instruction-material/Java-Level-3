package dispatch;
import dispatch.app.Desk;
import dispatch.ports.DeskApi;
import dispatch.ports.NoticeSink;
public final class Composition {
    private Composition() {}
    public static DeskApi create(NoticeSink sink) { return new Desk(sink); }
}
