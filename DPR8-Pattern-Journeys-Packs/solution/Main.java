import notices.*;
import tickets.*;
import editor.*;
import java.util.List;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode = args.length == 0 ? "core" : args[0];
        switch (mode) {
            case "baseline", "core" -> {
                NoticeAPI service = mode.equals("baseline") ? new LegacyNotices() : new NoticeService();
                System.out.print(service.render("bracket", List.of(new Notice("lamp", 2), new Notice("kit", 3)), true));
            }
            case "transfer-baseline", "transfer" -> {
                TicketAPI ticket = mode.endsWith("baseline") ? new LegacyTicket() : new Ticket();
                for (String action : List.of("finish", "approve", "approve", "finish", "reset"))
                    System.out.println(action + "=" + ticket.apply(action) + ":" + ticket.status());
                System.out.println(ticket.history());
            }
            case "extension-baseline", "extension" -> {
                EditorAPI editor = mode.endsWith("baseline") ? new LegacyEditor() : new Editor();
                editor.append(2026, 11, 15, "lamp"); editor.append(2026, 11, 16, "kit");
                System.out.print(editor.text());
                System.out.println("undo=" + editor.undo()); System.out.print(editor.text());
                System.out.println("redo=" + editor.redo()); System.out.print(editor.text());
            }
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
