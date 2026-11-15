package app;
import api.*;
public final class GreetingService implements Greetings {
    public GreetingService() {}
    public String hello(Profile profile) {
        if(profile==null)return "Hello, guest";
        return "Hello, "+profile.name();
    }
    public String channel(Profile profile) {
        if(profile==null)return "screen";
        return profile.channel();
    }
}
