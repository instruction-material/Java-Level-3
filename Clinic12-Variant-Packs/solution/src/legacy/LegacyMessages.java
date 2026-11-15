package legacy;
import api.*;
import java.util.Objects;
public final class LegacyMessages implements Messages {
    public LegacyMessages() {}
    public String render(String mode,String message) {
        Objects.requireNonNull(mode,"mode");Rules.text(message);
        if(mode.equals("plain"))return "text="+message+"\n";
        if(mode.equals("compact"))return message.length()+"|"+message+"\n";
        throw new IllegalArgumentException("mode");
    }
}
