package app;
import api.*;
import java.util.*;
public final class MessageService implements Messages {
    private final Map<String,RenderRule> rules;
    public MessageService() { this(Map.of("plain",new PlainRule(),"compact",new CompactRule())); }
    public MessageService(Map<String,RenderRule> rules) {
        Objects.requireNonNull(rules,"rules");
        Map<String,RenderRule> copy=new LinkedHashMap<>();
        for(Map.Entry<String,RenderRule> entry:rules.entrySet()) {
            Rules.key(entry.getKey());Objects.requireNonNull(entry.getValue(),"rule");
            copy.put(entry.getKey(),entry.getValue());
        }
        this.rules=Collections.unmodifiableMap(copy);
    }
    public String render(String mode,String message) {
        Objects.requireNonNull(mode,"mode");Rules.text(message);
        RenderRule rule=rules.get(mode);
        if(rule==null)throw new IllegalArgumentException("mode");
        return rule.render(message);
    }
}
