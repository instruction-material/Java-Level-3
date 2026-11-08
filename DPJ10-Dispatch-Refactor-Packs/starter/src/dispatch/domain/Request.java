package dispatch.domain;
import java.util.Objects;
/** One bounded, immutable dispatch request; all monetary values are teaching cents. */
public record Request(String id,String text,Channel channel,Priority priority,int copies) {
    public enum Channel { EMAIL, SMS }
    public enum Priority { NORMAL, URGENT }
    public Request {
        id=key(id);text=line(text);
        Objects.requireNonNull(channel,"channel");Objects.requireNonNull(priority,"priority");
        if(copies<1 || copies>20)throw new IllegalArgumentException("copies must be 1..20");
    }
    public static String key(String value) {
        Objects.requireNonNull(value,"id");
        if(!value.matches("[a-z][a-z0-9-]*"))throw new IllegalArgumentException("invalid id");
        return value;
    }
    public static String line(String value) {
        Objects.requireNonNull(value,"text");
        if(value.indexOf('\r')>=0 || value.indexOf('\n')>=0)throw new IllegalArgumentException("single-line text");
        return value;
    }
}
