import api.*;
import app.GreetingService;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        Greetings greetings=new GreetingService();
        if(!greetings.hello(null).equals("Hello, guest")||!greetings.channel(null).equals("screen"))
            throw new AssertionError("absence policy");
        System.out.println("compatibility passed");
    }
}
