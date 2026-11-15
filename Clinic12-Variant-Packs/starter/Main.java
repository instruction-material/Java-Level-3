import api.*;
import app.MessageService;
import legacy.LegacyMessages;
import transfer.RenderBatch;
import extension.PrefixRule;
import java.util.List;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> System.out.print(new LegacyMessages().render("plain","go"));
            case "core" -> System.out.print(new MessageService().render("plain","go"));
            case "transfer" -> System.out.print(RenderBatch.lines(new LegacyMessages(),"compact",List.of("go","")));
            case "extension" -> System.out.print(new PrefixRule("!",s->"text="+s+"\n").render("go"));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
