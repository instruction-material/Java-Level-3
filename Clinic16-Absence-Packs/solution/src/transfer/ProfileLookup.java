package transfer;
import api.Profile;
import java.util.*;
public final class ProfileLookup {
    private ProfileLookup() {}
    private static void key(String value,String label) {
        Objects.requireNonNull(value,label);
        if(!value.matches("[a-z]{1,8}"))throw new IllegalArgumentException(label);
    }
    public static Optional<Profile> find(Map<String,Profile> directory,String key) {
        Objects.requireNonNull(directory,"directory");key(key,"key");
        Map<String,Profile> copy=new LinkedHashMap<>();
        for(Map.Entry<String,Profile> entry:directory.entrySet()) {
            key(entry.getKey(),"entryKey");
            copy.put(entry.getKey(),Objects.requireNonNull(entry.getValue(),"profile"));
        }
        return Optional.ofNullable(copy.get(key));
    }
}
