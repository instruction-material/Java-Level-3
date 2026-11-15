package api;
import java.util.Objects;
public final class Rules {
    private Rules() {}
    public static void text(String message) {
        Objects.requireNonNull(message,"message");
        if(message.length()>24||message.indexOf('\r')>=0||message.indexOf('\n')>=0||message.indexOf('|')>=0)
            throw new IllegalArgumentException("message");
    }
    public static void key(String mode) {
        Objects.requireNonNull(mode,"mode");
        if(!mode.matches("[a-z]{1,12}"))throw new IllegalArgumentException("mode");
    }
}
