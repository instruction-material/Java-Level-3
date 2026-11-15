package events;
public final class EventFactory {
    private EventFactory() {}
    public static EventBufferAPI create(int capacity) { return new DefaultEventBuffer(capacity); }
}
/** This layer contributes no state, behavior or separate contract. */
final class DefaultEventBuffer extends EventBuffer {
    private static final long serialVersionUID = 1L;
    DefaultEventBuffer(int capacity) { super(capacity); }
}
