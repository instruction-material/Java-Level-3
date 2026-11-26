package checks;
import tasks.Runner;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected,actual)) throw new AssertionError(expected + " != " + actual);
    }

    public static void main(String[] args) throws InterruptedException {
        var values = new ArrayList<>(List.of(2, 3));
        var first = new Runner.Job("oak", values);
        var second = new Runner.Job("cedar", List.of(-1));
        values.clear();
        var runner = new Runner();
        var result = runner.run(List.of(first, second), 2, Runner::sum);
        same(List.of("oak","cedar"), result.stream().map(Runner.Result::id).toList());
        same(OptionalLong.of(5), result.getFirst().value());
        same("Negative value", result.getLast().error());
        same(true, result.stream().allMatch(row -> row.elapsedNanos() >= 0));
        same(List.of(), runner.run(List.of(), 1, Runner::sum));

        var quickFinished = new CountDownLatch(1);
        var inverted = runner.run(List.of(first, second), 2, job -> {
            if (job.id().equals("cedar")) {
                quickFinished.countDown();
                return 2;
            }
            if (!quickFinished.await(3, TimeUnit.SECONDS)) throw new IllegalStateException("Second worker missing");
            return 1;
        });
        same(List.of(OptionalLong.of(1), OptionalLong.of(2)), inverted.stream().map(Runner.Result::value).toList());
        try {
            runner.run(List.of(first), 0, Runner::sum);
            throw new AssertionError("Expected worker validation");
        } catch (IllegalArgumentException expected) {
            same("Expected 1 to 8 workers", expected.getMessage());
        }
        System.out.println("Runner checks passed");
    }
}
