package events;
public interface CounterAPI {
    void add(long amount);
    long value();
    void reset();
}
