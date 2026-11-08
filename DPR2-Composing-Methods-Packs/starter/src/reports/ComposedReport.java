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
            trace.accept("read:" + i);
            long temporary = next.getAsInt();
            if (temporary > 0) positive++;
            if (includeExtra && temporary > 0) {
                trace.accept("bonus:" + i);
                temporary = temporary + temporary / 10;
            }
            total = total + temporary;
        }
        long mean = 0;
        if (count > 0) mean = total / count;
        return new Summary(count, positive, total, mean);
    }
    public static Summary summarizeValues(int[] values, boolean includeExtra) {
        throw new UnsupportedOperationException("Implement optional array adapter.");
    }
}
