package reports;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public final class ComposedReport {
    private ComposedReport() {}
    public static Summary summarize(int count, boolean includeExtra,
                                    IntSupplier next, Consumer<String> trace) {
        if (count < 0 || count > 1000) throw new IllegalArgumentException("count");
        Objects.requireNonNull(next, "next");
        Objects.requireNonNull(trace, "trace");
        long total = 0;
        int positive = 0;
        for (int i = 0; i < count; i++) {
            int sample = readSample(i, next, trace);
            if (sample > 0) positive++;
            long adjusted = adjustedValue(sample, i, includeExtra, trace);
            total += adjusted;
        }
        return new Summary(count, positive, total, mean(total, count));
    }
    private static int readSample(int index, IntSupplier next, Consumer<String> trace) {
        trace.accept("read:" + index);
        return next.getAsInt();
    }
    private static long adjustedValue(int sample, int index, boolean includeExtra,
                                      Consumer<String> trace) {
        long adjusted = sample;
        if (includeExtra && sample > 0) {
            trace.accept("bonus:" + index);
            adjusted += sample / 10;
        }
        return adjusted;
    }
    private static long mean(long total, int count) {
        return count == 0 ? 0 : total / count;
    }
    public static Summary summarizeValues(int[] values, boolean includeExtra) {
        int[] snapshot = Objects.requireNonNull(values, "values").clone();
        int[] cursor = {0};
        return summarize(snapshot.length, includeExtra, () -> snapshot[cursor[0]++], event -> {});
    }
}
