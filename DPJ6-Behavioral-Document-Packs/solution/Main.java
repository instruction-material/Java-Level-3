import behavior.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Document document = new Document("Ready");
        document.submit(); document.revise(); document.replace("Checked");
        document.submit(); document.approve();
        System.out.println(document.phase());
        System.out.println(new RuleContext(new Rules.Prefix("Review: ")).format(document.text()));
        System.out.print(new Reports.Upper().render(List.of(document.text(), "Done")));
        ReviewQueue queue = new ReviewQueue(); queue.add("first"); queue.add("second");
        for (String entry : queue) System.out.println(entry);
    }
}
