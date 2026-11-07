import java.util.List;
import creation.Report;
import creation.ReportJob;
import creation.Style;
public class Main {
    public static void main(String[] args) {
        ReportJob job = new ReportJob.Builder(new Report("Notes", List.of("one", "two")))
            .style(Style.BOXED).includeCount(true).footer("End").build();
        System.out.print(job.render());
    }
}
