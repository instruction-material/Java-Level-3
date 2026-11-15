package transfer;
import api.Profile;
import java.util.*;
public final class ProfileLookup {
    private ProfileLookup() {}
    public static Optional<Profile> find(Map<String,Profile> directory,String key) {
        throw new UnsupportedOperationException("Complete explicit lookup");
    }
}
