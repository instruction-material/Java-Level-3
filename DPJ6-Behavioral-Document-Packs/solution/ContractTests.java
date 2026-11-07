import behavior.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        checks++;
        if (!Objects.equals(actual, expected)) throw new AssertionError(actual + " != " + expected);
    }
    private static void yes(boolean value) { equal(value, true); }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Expected " + type.getName() + ", got " + error, error);
        }
        throw new AssertionError("Expected " + type.getName());
    }
    private static void core() {
        equal(new RuleContext(new Rules.Identity()).format(" Ready "), " Ready ");
        equal(new RuleContext(new Rules.Upper()).format("Ready"), "READY");
        equal(new RuleContext(new Rules.Prefix("Review: ")).format("Ready"), "Review: Ready");
        equal(new RuleContext(String::strip).format(" Ready "), "Ready");
        equal(new RuleContext(value -> "").format("Ready"), "");
        int[] calls = {0};
        RuleContext selected = new RuleContext(value -> { calls[0]++; equal(value, "Ready"); return "Selected"; });
        equal(selected.format("Ready"), "Selected"); equal(calls[0], 1);
        fails(NullPointerException.class, () -> new RuleContext(null));
        fails(NullPointerException.class, () -> selected.format(null)); equal(calls[0], 1);
        fails(IllegalArgumentException.class, () -> selected.format("bad\nline")); equal(calls[0], 1);
        fails(NullPointerException.class, () -> new RuleContext(value -> null).format("a"));
        fails(IllegalArgumentException.class, () -> new RuleContext(value -> "bad\rline").format("a"));
        fails(IllegalStateException.class, () -> new RuleContext(value -> { throw new IllegalStateException(); }).format("a"));
        fails(NullPointerException.class, () -> new Rules.Prefix(null));
        fails(IllegalArgumentException.class, () -> new Rules.Prefix("bad\n"));
        Document doc = new Document("Ready"), independent = new Document("Other");
        equal(doc.phase(), "DRAFT"); equal(doc.text(), "Ready");
        fails(IllegalStateException.class, doc::approve); fails(IllegalStateException.class, doc::revise);
        equal(doc.phase(), "DRAFT"); equal(doc.text(), "Ready");
        doc.replace("Edited"); equal(doc.text(), "Edited"); doc.submit();
        equal(doc.phase(), "REVIEW"); equal(independent.phase(), "DRAFT");
        fails(IllegalStateException.class, () -> doc.replace("No")); fails(IllegalStateException.class, doc::submit);
        equal(doc.text(), "Edited"); equal(doc.phase(), "REVIEW");
        doc.revise(); equal(doc.phase(), "DRAFT"); doc.replace(""); equal(doc.text(), "");
        doc.submit(); doc.approve(); equal(doc.phase(), "PUBLISHED");
        for (Runnable action : List.<Runnable>of(doc::submit, doc::approve, doc::revise, () -> doc.replace("No")))
            fails(IllegalStateException.class, action);
        equal(doc.text(), ""); equal(doc.phase(), "PUBLISHED");
        fails(NullPointerException.class, () -> new Document(null));
        fails(IllegalArgumentException.class, () -> new Document("bad\nline"));
        fails(NullPointerException.class, () -> independent.replace(null));
        fails(IllegalArgumentException.class, () -> independent.replace("bad\rline"));
        equal(independent.text(), "Other"); equal(independent.phase(), "DRAFT");
        equal(new Reports.Plain().render(List.of()), "Report\nEnd\n");
        equal(new Reports.Plain().render(List.of("a", "")), "Report\na\n\nEnd\n");
        equal(new Reports.Upper().render(List.of("a", "Ready")), "UPPER\nA\nREADY\nEnd\n");
        List<String> order = new ArrayList<>(), caller = new ArrayList<>(List.of("a", "b"));
        ReportTemplate traced = new ReportTemplate() {
            protected String heading() { order.add("head"); caller.clear(); return "H"; }
            protected String formatLine(String text) { order.add("line:" + text); return text; }
            protected String footer() { order.add("foot"); return "F"; }
        };
        equal(traced.render(caller), "H\na\nb\nF\n"); equal(order, List.of("head","line:a","line:b","foot"));
        order.clear();
        fails(NullPointerException.class, () -> traced.render(null)); yes(order.isEmpty());
        fails(NullPointerException.class, () -> traced.render(Arrays.asList("a",null))); yes(order.isEmpty());
        fails(IllegalArgumentException.class, () -> traced.render(List.of("bad\nline"))); yes(order.isEmpty());
        ReportTemplate nullHead = new ReportTemplate() {
            protected String heading() { return null; }
            protected String formatLine(String text) { throw new AssertionError("line must not run"); }
        };
        fails(NullPointerException.class, () -> nullHead.render(List.of("a")));
        List<String> failureTrace = new ArrayList<>();
        ReportTemplate badLine = new ReportTemplate() {
            protected String heading() { failureTrace.add("head"); return "H"; }
            protected String formatLine(String text) { failureTrace.add(text); return null; }
            protected String footer() { throw new AssertionError("footer must not run"); }
        };
        fails(NullPointerException.class, () -> badLine.render(List.of("a","b"))); equal(failureTrace, List.of("head","a"));
        ReportTemplate badFooter = new ReportTemplate() {
            protected String heading() { return "H"; }
            protected String formatLine(String text) { return text; }
            protected String footer() { return "bad\r"; }
        };
        fails(IllegalArgumentException.class, () -> badFooter.render(List.of("a")));
        ReviewQueue queue = new ReviewQueue(); Iterator<String> empty = queue.iterator();
        yes(!empty.hasNext()); fails(NoSuchElementException.class, empty::next);
        fails(UnsupportedOperationException.class, empty::remove);
        queue.add("first"); queue.add("second"); queue.add("first");
        equal(queue.size(), 3); yes(!empty.hasNext());
        Iterator<String> first = queue.iterator(), second = queue.iterator();
        yes(first != second); yes(first.hasNext()); yes(first.hasNext());
        equal(first.next(), "first"); equal(second.next(), "first");
        queue.add("third");
        equal(first.next(), "second"); equal(first.next(), "first");
        yes(!first.hasNext()); fails(NoSuchElementException.class, first::next); yes(!first.hasNext());
        fails(UnsupportedOperationException.class, second::remove);
        List<String> tail = new ArrayList<>(); second.forEachRemaining(tail::add); equal(tail, List.of("second","first"));
        List<String> latest = new ArrayList<>(); for (String value : queue) latest.add(value);
        equal(latest, List.of("first","second","first","third"));
        fails(NullPointerException.class, () -> queue.add(null));
        fails(IllegalArgumentException.class, () -> queue.add("bad\n")); equal(queue.size(),4);
    }
    private static void extension() {
        ReviewQueue queue = new ReviewQueue(); queue.add("a"); queue.add("b"); queue.add("a");
        List<String> visited = new ArrayList<>();
        List<String> matched = queue.matching(value -> { visited.add(value); if (visited.size()==1) queue.add("later"); return value.equals("a"); });
        equal(matched,List.of("a","a")); equal(visited,List.of("a","b","a")); equal(queue.size(),4);
        fails(UnsupportedOperationException.class, () -> matched.add("x"));
        fails(NullPointerException.class, () -> queue.matching(null));
        List<String> failureTrace = new ArrayList<>();
        fails(IllegalStateException.class, () -> queue.matching(value -> { failureTrace.add(value); throw new IllegalStateException(); }));
        equal(failureTrace,List.of("a"));
        equal(queue.matching(value -> false),List.of()); equal(queue.matching(value -> true),List.of("a","b","a","later"));
    }
    public static void main(String[] args) {
        boolean coreOnly = args.length==1 && args[0].equals("core");
        if (!(coreOnly || args.length==0)) throw new IllegalArgumentException("mode must be core or empty");
        core(); if (!coreOnly) extension();
        System.out.println((coreOnly ? "Core behavioral" : "Complete behavioral") + " contracts: " + checks + " passed");
    }
}
