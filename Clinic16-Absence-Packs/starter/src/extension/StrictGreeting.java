package extension;
import api.Profile;
import java.util.Optional;
public final class StrictGreeting {
    private StrictGreeting() {}
    public static String hello(Optional<Profile> result) {throw new UnsupportedOperationException("Complete required profile policy");}
}
