package runner;
import tasks.Runner;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var jobs = List.of(new Runner.Job("oak", List.of(2, 3)),
                new Runner.Job("cedar", List.of(-1)), new Runner.Job("elm", List.of()));
        var runner = new Runner();
        for (var result : runner.run(jobs, 2, Runner::sum)) {
            System.out.println(result.id() + " " + (result.value().isPresent()
                    ? Long.toString(result.value().orElseThrow()) : result.error()));
        }
    }
}
