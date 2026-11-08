import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import reports.ComposedReport;
import reports.LegacyReport;
import reports.Summary;
import reports.comparison.MethodObjectReport;

public final class ContractTests {
    private static int checks;
    private ContractTests() {}
    @FunctionalInterface
    private interface Report {
        Summary run(int count, boolean extra, IntSupplier next, Consumer<String> trace);
    }
    private static void same(Object expected, Object actual) {
        checks++;
        if (!expected.equals(actual)) throw new AssertionError("Expected " + expected + "; got " + actual);
    }
    private static void raises(Class<? extends Throwable> expected, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable failure) {
            if (expected.isInstance(failure)) return;
            throw new AssertionError("Wrong exception " + failure, failure);
        }
        throw new AssertionError("Missing " + expected.getSimpleName());
    }
    private static void identical(Throwable expected, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable failure) {
            if (failure == expected) return;
            throw new AssertionError("Exception identity changed", failure);
        }
        throw new AssertionError("Missing original exception");
    }
    private static void sequence(Report report, int[] values, boolean extra) {
        List<String> expectedEvents = new ArrayList<>();
        long total = 0;
        int positives = 0;
        for (int i = 0; i < values.length; i++) {
            expectedEvents.add("read:" + i);
            long sample = values[i];
            if (sample > 0) {
                positives++;
                if (extra) {
                    expectedEvents.add("bonus:" + i);
                    total += sample / 10;
                }
            }
            total += sample;
        }
        Summary expected = new Summary(values.length, positives, total,
                                       values.length == 0 ? 0 : total / values.length);
        List<String> events = new ArrayList<>();
        int[] cursor = {0};
        Summary actual = report.run(values.length, extra, () -> {
            events.add("supply:" + cursor[0]);
            return values[cursor[0]++];
        }, events::add);
        List<String> interleaved = new ArrayList<>();
        for (String event : expectedEvents) {
            interleaved.add(event);
            if (event.startsWith("read:")) interleaved.add("supply:" + event.substring(5));
        }
        same(expected, actual);
        same(values.length, cursor[0]);
        same(interleaved, events);
        same("count=" + values.length + "|positive=" + positives + "|total=" + total
             + "|mean=" + expected.mean(), actual.format());
    }
    private static void core(Report report) {
        int[] domain = {-11, -1, 0, 1, 9, 10, 11, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (boolean extra : new boolean[] {false, true}) {
            sequence(report, new int[] {}, extra);
            for (int a : domain) {
                sequence(report, new int[] {a}, extra);
                for (int b : domain) {
                    sequence(report, new int[] {a, b}, extra);
                    for (int c : domain) sequence(report, new int[] {a, b, c}, extra);
                }
            }
            int[] maximum = new int[1000];
            Arrays.fill(maximum, Integer.MAX_VALUE);
            sequence(report, maximum, extra);
            Arrays.fill(maximum, Integer.MIN_VALUE);
            sequence(report, maximum, extra);
        }
        for (int bad : new int[] {-1, 1001, Integer.MIN_VALUE, Integer.MAX_VALUE})
            raises(IllegalArgumentException.class, () -> report.run(bad, true, null, null));
        raises(NullPointerException.class, () -> report.run(0, true, null, null));
        raises(NullPointerException.class, () -> report.run(0, true, () -> 1, null));
        for (int count : new int[] {0, 1, 1000}) {
            List<String> events = new ArrayList<>();
            RuntimeException readFailure = new IllegalStateException("supplier");
            int[] calls = {0};
            if (count > 0) {
                identical(readFailure, () -> report.run(count, true, () -> {
                    calls[0]++; throw readFailure;
                }, events::add));
                same(List.of("read:0"), events);
                same(1, calls[0]);
            }
            events.clear();
            RuntimeException traceFailure = new IllegalArgumentException("trace");
            if (count > 0) {
                calls[0] = 0;
                identical(traceFailure, () -> report.run(count, true, () -> {
                    calls[0]++; return 20;
                }, event -> { events.add(event); throw traceFailure; }));
                same(0, calls[0]);
                same(List.of("read:0"), events);
            } else {
                same(new Summary(0, 0, 0, 0),
                     report.run(0, true, () -> { throw readFailure; }, event -> { throw traceFailure; }));
            }
        }
        List<String> events = new ArrayList<>();
        int[] calls = {0};
        RuntimeException bonusFailure = new IllegalStateException("bonus");
        identical(bonusFailure, () -> report.run(2, true, () -> {
            calls[0]++; return 20;
        }, event -> { events.add(event); if (event.startsWith("bonus:")) throw bonusFailure; }));
        same(List.of("read:0", "bonus:0"), events);
        same(1, calls[0]);
        // The first complete sample remains observable before the second supplier fails.
        events.clear(); calls[0] = 0;
        RuntimeException secondFailure = new IllegalStateException("second");
        identical(secondFailure, () -> report.run(3, true, () -> {
            if (calls[0]++ == 1) throw secondFailure;
            return 20;
        }, events::add));
        same(List.of("read:0", "bonus:0", "read:1"), events);
        same(2, calls[0]);
        sequence(report, new int[] {-1, 0}, false); // Java integer division truncates toward zero.
        sequence(report, new int[] {11, -11, 0}, true); // Repeated calls retain no computation state.
    }
    private static void optional() {
        int[] input = {12, -5, 20};
        same(new Summary(3, 2, 30, 10), ComposedReport.summarizeValues(input, true));
        same("[12, -5, 20]", Arrays.toString(input));
        Summary previous = ComposedReport.summarizeValues(input, true);
        input[0] = 100;
        same(new Summary(3, 2, 30, 10), previous);
        same(new Summary(0, 0, 0, 0), ComposedReport.summarizeValues(new int[] {}, true));
        same(new Summary(2, 0, -1, 0), ComposedReport.summarizeValues(new int[] {-1, 0}, true));
        raises(NullPointerException.class, () -> ComposedReport.summarizeValues(null, false));
        raises(IllegalArgumentException.class, () -> ComposedReport.summarizeValues(new int[1001], true));
        int[] maximum = new int[1000];
        Arrays.fill(maximum, Integer.MAX_VALUE);
        same(new Summary(1000, 1000, 2362232011000L, 2362232011L),
             ComposedReport.summarizeValues(maximum, true));
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!List.of("base", "core", "object", "all").contains(mode)) throw new IllegalArgumentException("mode");
        if (mode.equals("object")) core(MethodObjectReport::summarize);
        else {
            core(LegacyReport::summarize);
            if (!mode.equals("base")) core(ComposedReport::summarize);
            if (mode.equals("all")) optional();
        }
        String label = mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core"
            : mode.equals("object") ? "Method-object" : "Complete";
        System.out.println(label + " composing-methods contracts: " + checks + " passed");
    }
}
