import app.DocumentService;
import legacy.LegacyDocuments;
import transfer.WorkflowTrace;
import extension.NumberedRows;
import java.util.List;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> System.out.print(new LegacyDocuments().build("plain",List.of("a","b")));
            case "core" -> System.out.print(new DocumentService().build("plain",List.of("a","b")));
            case "transfer" -> System.out.println(WorkflowTrace.stages(List.of("a","b"),(phase,value)->{}));
            case "extension" -> System.out.print(NumberedRows.render(List.of("a","b"),true));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
