package reports.comparison;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import reports.Summary;

/** Complete comparison: per-call computation state, never shared across calls. */
public final class MethodObjectReport {
    private MethodObjectReport() {}
    public static Summary summarize(int count, boolean includeExtra,
                                    IntSupplier next, Consumer<String> trace) {
        if (count < 0 || count > 1000) throw new IllegalArgumentException("count");
        Objects.requireNonNull(next, "next");
        Objects.requireNonNull(trace, "trace");
        return new Computation(count, includeExtra, next, trace).compute();
    }
    private static final class Computation {
        private final int count;
        private final boolean includeExtra;
        private final IntSupplier next;
        private final Consumer<String> trace;
        private long total;
        private int positive;

        Computation(int count, boolean includeExtra, IntSupplier next, Consumer<String> trace) {
            this.count = count;
            this.includeExtra = includeExtra;
            this.next = next;
            this.trace = trace;
        }
        Summary compute() {
            for (int i = 0; i < count; i++) collect(i);
            return new Summary(count, positive, total, count == 0 ? 0 : total / count);
        }
        private void collect(int index) {
            trace.accept("read:" + index);
            int sample = next.getAsInt();
            if (sample > 0) positive++;
            long adjusted = sample;
            if (includeExtra && sample > 0) {
                trace.accept("bonus:" + index);
                adjusted += sample / 10;
            }
            total += adjusted;
        }
    }
}
