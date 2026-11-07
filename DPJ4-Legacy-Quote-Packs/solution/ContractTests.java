import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import boundary.*;

public class ContractTests {
    private static int checks;
    private static void yes(boolean condition) {
        if (!condition) throw new AssertionError("expected true");
        checks++;
    }
    private static void equal(Object actual, Object expected) {
        if (!actual.equals(expected)) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("wrong failure", error);
            checks++; return;
        }
        throw new AssertionError("missing " + type.getSimpleName());
    }
    private static void core() {
        List<OrderDraft.Line> input = new ArrayList<>(List.of(new OrderDraft.Line("book", 6)));
        OrderDraft draft = new OrderDraft("Class order", input);
        input.get(0).setUnits(2); input.add(new OrderDraft.Line("pen", 3));
        equal(draft.totalUnits(), 6);
        OrderDraft copy = draft.copy();
        yes(copy != draft); yes(copy.lines() != draft.lines()); yes(copy.lines().get(0) != draft.lines().get(0));
        copy.lines().get(0).setUnits(9); copy.add("pen", 1);
        equal(draft.totalUnits(), 6); equal(copy.totalUnits(), 10);
        equal(copy.title(), draft.title()); equal(copy.lines().get(0).name(), "book");
        fails(UnsupportedOperationException.class, () -> copy.lines().clear());
        fails(NullPointerException.class, () -> new OrderDraft(null, List.of()));
        fails(NullPointerException.class, () -> new OrderDraft("T", null));
        fails(NullPointerException.class, () -> new OrderDraft("T", java.util.Arrays.asList((OrderDraft.Line) null)));
        for (String text : List.of("", " ", "bad\nname", "bad\rname")) {
            fails(IllegalArgumentException.class, () -> new OrderDraft(text, List.of()));
            fails(IllegalArgumentException.class, () -> new OrderDraft.Line(text, 1));
        }
        for (int units : new int[]{0, -1, 1001})
            fails(IllegalArgumentException.class, () -> new OrderDraft.Line("x", units));
        PriceAdapter adapter = new PriceAdapter(new LegacyPrice.Standard());
        QuoteFacade facade = new QuoteFacade(adapter);
        for (int units = 0; units <= 1000; units++) {
            long cents = (long) units * (units < 5 ? 350 : units < 10 ? 300 : 250);
            BigDecimal expected = BigDecimal.valueOf(cents, 2);
            equal(adapter.total(units), expected);
            OrderDraft request = new OrderDraft("Order", List.of());
            if (units > 0) request.add("books", units);
            QuoteFacade.Quote quote = facade.quote(request);
            equal(quote.total(), expected); equal(quote.units(), units);
            equal(quote.render(), "Order: " + units + " units = USD " + expected.toPlainString() + "\n");
        }
        int[] calls = {0}; List<String> received = new ArrayList<>();
        PriceAdapter spy = new PriceAdapter(text -> { calls[0]++; received.add(text); return "USD 18.00"; });
        equal(spy.total(6), new BigDecimal("18.00")); equal(received, List.of("6")); equal(calls[0], 1);
        fails(IllegalArgumentException.class, () -> spy.total(-1)); equal(calls[0], 1);
        fails(IllegalArgumentException.class, () -> spy.total(1001)); equal(calls[0], 1);
        fails(NullPointerException.class, () -> new PriceAdapter(null));
        fails(NullPointerException.class, () -> new PriceAdapter(text -> null).total(6));
        for (String result : List.of("EUR 18.00", "USD -1.00", "USD 18", "USD 18.0", "USD 18.000", "USD 1e2", "USD NaN", "USD 18.00\n"))
            fails(IllegalArgumentException.class, () -> new PriceAdapter(text -> result).total(6));
        fails(IllegalStateException.class, () -> new PriceAdapter(text -> { throw new IllegalStateException("unavailable"); }).total(6));
        int[] priced = {0};
        QuoteFacade recorder = new QuoteFacade(units -> { priced[0]++; return new BigDecimal("18.00"); });
        QuoteFacade.Quote frozen = recorder.quote(draft);
        equal(priced[0], 1); equal(frozen.units(), 6);
        draft.lines().get(0).setUnits(9); equal(frozen.units(), 6);
        fails(NullPointerException.class, () -> recorder.quote(null)); equal(priced[0], 1);
        OrderDraft tooLarge = new OrderDraft("T", List.of(new OrderDraft.Line("a", 600), new OrderDraft.Line("b", 600)));
        fails(IllegalArgumentException.class, () -> recorder.quote(tooLarge)); equal(priced[0], 1);
        fails(NullPointerException.class, () -> new QuoteFacade(null));
        fails(NullPointerException.class, () -> new QuoteFacade(units -> null).quote(draft));
        fails(IllegalArgumentException.class, () -> new QuoteFacade(units -> new BigDecimal("-1.00")).quote(draft));
        fails(IllegalArgumentException.class, () -> new QuoteFacade(units -> new BigDecimal("1.0")).quote(draft));
        fails(IllegalStateException.class, () -> new QuoteFacade(units -> { throw new IllegalStateException("unavailable"); }).quote(draft));
        equal(new QuoteFacade(units -> new BigDecimal("1.00")).quote(draft).total(), new BigDecimal("1.00"));
        equal(new QuoteFacade(units -> new BigDecimal("2.00")).quote(draft).total(), new BigDecimal("2.00"));
        equal(LifetimeExamples.CurrencySettings.INSTANCE.currency(), "USD");
        yes(LifetimeExamples.CurrencySettings.INSTANCE == LifetimeExamples.CurrencySettings.INSTANCE);
        LifetimeExamples.Counter first = new LifetimeExamples.Counter(), second = new LifetimeExamples.Counter();
        equal(first.next(), 1); equal(first.next(), 2); equal(second.next(), 1);
    }
    private static void extension() {
        OrderDraft original = new OrderDraft("Original", List.of(new OrderDraft.Line("book", 6)));
        OrderDraft variant = original.copyWithTitle("Variant");
        equal(variant.title(), "Variant"); equal(original.title(), "Original"); equal(variant.totalUnits(), 6);
        variant.lines().get(0).setUnits(9); equal(original.totalUnits(), 6); equal(variant.totalUnits(), 9);
        fails(NullPointerException.class, () -> original.copyWithTitle(null));
        fails(IllegalArgumentException.class, () -> original.copyWithTitle(""));
    }
    public static void main(String[] args) {
        boolean onlyCore = args.length == 1 && args[0].equals("core");
        if (!(onlyCore || args.length == 0)) throw new IllegalArgumentException("mode must be core or empty");
        core(); if (!onlyCore) extension();
        System.out.println((onlyCore ? "Core boundary" : "Complete boundary") + " contracts: " + checks + " passed");
    }
}
