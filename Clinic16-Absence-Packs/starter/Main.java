import api.*;
import app.GreetingService;
import legacy.LegacyGreetings;
import transfer.ProfileLookup;
import extension.StrictGreeting;
import java.util.*;
public final class Main {
    private Main() {}
    private static void demo(Greetings greetings) {
        System.out.println(greetings.hello(null)+"|"+greetings.channel(null));
        Profile ada=new Profile("Ada","email");
        System.out.println(greetings.hello(ada)+"|"+greetings.channel(ada));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> demo(new LegacyGreetings());
            case "core" -> demo(new GreetingService());
            case "transfer" -> System.out.println(ProfileLookup.find(Map.of("ada",new Profile("Ada","email")),"ada"));
            case "extension" -> System.out.println(StrictGreeting.hello(Optional.of(new Profile("Ada","email"))));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
