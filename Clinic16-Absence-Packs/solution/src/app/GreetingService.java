package app;
import api.*;
public final class GreetingService implements Greetings {
    public GreetingService() {}
    public String hello(Profile profile) {return "Hello, "+Recipient.of(profile).label();}
    public String channel(Profile profile) {return Recipient.of(profile).channel();}
}
