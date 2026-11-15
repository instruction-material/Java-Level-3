import api.*;
import app.BoardReport;
import legacy.LegacyBoard;
import transfer.SnapshotDiff;
import extension.BoardPreview;
import java.util.List;
public final class Main {
    private Main() {}
    public static List<Task> sample() {return List.of(new Task("read",10,false),new Task("code",20,true),new Task("draw",5,false));}
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> System.out.print(new LegacyBoard().summary(sample()));
            case "core" -> System.out.print(new BoardReport().summary(sample()));
            case "transfer" -> System.out.println(SnapshotDiff.firstDifference(List.of("before","step1","final"),List.of("before","bad","final")));
            case "extension" -> System.out.println(BoardPreview.sorted(sample()).stream().map(Task::title).toList());
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
