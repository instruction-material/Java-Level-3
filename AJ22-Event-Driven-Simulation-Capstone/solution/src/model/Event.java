package model;

public sealed interface Event permits Event.Request, Event.Return, Event.Cancel {
    String member();
    String title();

    record Request(String member, String title) implements Event {}
    record Return(String member, String title) implements Event {}
    record Cancel(String member, String title) implements Event {}
}
