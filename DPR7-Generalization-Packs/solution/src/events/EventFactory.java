package events;
public final class EventFactory {
    private EventFactory() {}
    public static EventBufferAPI create(int capacity) { return new EventBuffer(capacity); }
}
