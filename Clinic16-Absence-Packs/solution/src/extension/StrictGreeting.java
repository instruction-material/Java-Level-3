package extension;
import api.Profile;
import java.util.*;
public final class StrictGreeting {
    private StrictGreeting() {}
    public static String hello(Optional<Profile> result) {
        Objects.requireNonNull(result,"result");
        Profile profile=result.orElseThrow(()->new NoSuchElementException("profile"));
        return "Hello, "+profile.name();
    }
}
