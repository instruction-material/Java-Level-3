package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Runner {
    public record Job(String id, List<Integer> values) {
        public Job { values = List.copyOf(values); }
    }

    public record Result(String id, OptionalLong value, String error, long elapsedNanos) {}

    @FunctionalInterface
    public interface Worker {
        long apply(Job job) throws Exception;
    }

    public List<Result> run(List<Job> jobs, int workers, Worker worker) throws InterruptedException {
        if (workers < 1 || workers > 8) throw new IllegalArgumentException("Expected 1 to 8 workers");
        throw new UnsupportedOperationException("Run bounded batches and return results in input order");
    }

    private Result attempt(Job job, Worker worker) {
        long start = System.nanoTime();
        try {
            long value = worker.apply(job);
            return new Result(job.id(), OptionalLong.of(value), "", System.nanoTime() - start);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            return new Result(job.id(), OptionalLong.empty(), "Interrupted", System.nanoTime() - start);
        } catch (Exception failure) {
            String message = failure.getMessage() == null ? failure.getClass().getSimpleName() : failure.getMessage();
            return new Result(job.id(), OptionalLong.empty(), message, System.nanoTime() - start);
        }
    }

    public static long sum(Job job) {
        long total = 0;
        for (int value : job.values()) {
            if (value < 0) throw new IllegalArgumentException("Negative value");
            total += value;
        }
        return total;
    }
}
