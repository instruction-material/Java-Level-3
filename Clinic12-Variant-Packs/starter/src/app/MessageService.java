package app;
import api.*;
import legacy.LegacyMessages;
import java.util.Map;
public final class MessageService implements Messages {
    public MessageService() {}
    public MessageService(Map<String,RenderRule> rules) {
        throw new UnsupportedOperationException("Complete rule registry");
    }
    public String render(String mode,String message) {
        java.util.Objects.requireNonNull(mode,"mode");Rules.text(message);
        if(mode.equals("plain"))return "text="+message+"\n";
        if(mode.equals("compact"))return message.length()+"|"+message+"\n";
        throw new IllegalArgumentException("mode");
    }
}
