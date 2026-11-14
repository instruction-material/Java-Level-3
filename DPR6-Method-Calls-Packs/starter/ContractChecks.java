import dispatch.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ContractChecks {
    private static int checks;
    private ContractChecks() {}
    public static void equal(Object expected, Object actual, String label) {
        checks++;
        if (!Objects.equals(expected, actual))
            throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
    }
    public static void error(String expected, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException failure) {
            equal(expected, failure.getClass().getSimpleName() + ":" + failure.getMessage(), "error");
            return;
        }
        throw new AssertionError("Missing " + expected);
    }
    private static int expected(int grams, int zone, boolean express, int discount) {
        int bands = grams / 500 + (grams % 500 == 0 ? 0 : 1);
        return zone * 100 + bands * 40 + (express ? 250 : 0) - discount;
    }
    private static void legacyInvalid(LegacyDesk desk) {
        List<Quote> before = desk.history();
        equal(-1, desk.price(null, 0, 0, true, null), "null label");
        equal(-1, desk.price(" ", 1, 1, false, null), "blank label");
        for (int grams : new int[] {0, 5001})
            equal(-1, desk.price("X", grams, 1, false, null), "invalid grams");
        for (int zone : new int[] {0, 4})
            equal(-1, desk.price("X", 1, zone, false, null), "invalid zone");
        equal(before, desk.history(), "failed operations preserve history");
        equal(null, desk.find(null), "null legacy lookup");
        equal(null, desk.find(" "), "blank legacy lookup");
        equal(null, desk.find("missing"), "absent legacy lookup");
        error("IllegalArgumentException:discountCents", () -> desk.setDiscount(-1));
        error("IllegalArgumentException:discountCents", () -> desk.setDiscount(101));
    }
    private static void modernInvalid(DispatchService service, DispatchLog log) {
        List<Quote> before = log.snapshot();
        error("NullPointerException:parcel", () -> service.quote(null, null));
        error("NullPointerException:speed", () -> service.quote(new Parcel("X", 1, 1), null));
        error("NullPointerException:parcel", () -> service.send(null, DeliverySpeed.STANDARD));
        error("NullPointerException:label", () -> new Parcel(null, 0, 0));
        error("IllegalArgumentException:label", () -> new Parcel(" ", 0, 0));
        error("IllegalArgumentException:grams", () -> new Parcel("X", 0, 0));
        error("IllegalArgumentException:grams", () -> new Parcel("X", 5001, 1));
        error("IllegalArgumentException:zone", () -> new Parcel("X", 1, 0));
        error("IllegalArgumentException:zone", () -> new Parcel("X", 1, 4));
        error("NullPointerException:log", () -> DispatchService.discounted(-1, null));
        error("IllegalArgumentException:discountCents", () -> DispatchService.discounted(101, log));
        error("NullPointerException:label", () -> log.find(null));
        error("IllegalArgumentException:label", () -> log.find(""));
        equal(before, log.snapshot(), "validation preserves log");
        equal(Optional.empty(), log.find("missing"), "absence is not null");
    }
    public static void main(String[] args) {
        if (args.length != 1 || !(args[0].equals("baseline") || args[0].equals("core")))
            throw new IllegalArgumentException("mode");
        boolean core = args[0].equals("core");
        int rows = 0;
        for (String label : new String[] {"A", "two words", "quote \" literal"})
            for (int grams : new int[] {1, 499, 500, 501, 999, 1000, 1001, 5000})
                for (int zone = 1; zone <= 3; zone++)
                    for (boolean express : new boolean[] {false, true})
                        for (int discount : new int[] {0, 1, 25, 100}) {
                            int cents = expected(grams, zone, express, discount);
                            Quote quote = new Quote(label, cents);
                            LegacyDesk legacy = new LegacyDesk(discount);
                            equal(cents, legacy.price(label, grams, zone, express, null), "oracle amount");
                            equal(List.of(quote), legacy.history(), "oracle history");
                            equal(cents, legacy.totalCents(), "oracle total");
                            equal(quote, legacy.find(label), "oracle lookup");
                            if (core) {
                                DispatchLog log = new DispatchLog();
                                DispatchService service = DispatchService.discounted(discount, log);
                                Parcel parcel = new Parcel(label, grams, zone);
                                DeliverySpeed speed = express ? DeliverySpeed.EXPRESS : DeliverySpeed.STANDARD;
                                equal(quote, service.quote(parcel, speed), "pure quote");
                                equal(quote, service.quote(parcel, speed), "repeated query");
                                equal(List.of(), log.snapshot(), "query has no writes");
                                equal(quote, service.send(parcel, speed), "send result");
                                List<Quote> earlier = log.snapshot();
                                equal(quote, service.send(parcel, speed), "repeated send");
                                equal(List.of(quote, quote), log.snapshot(), "one write per send");
                                equal(List.of(quote), earlier, "earlier snapshot unchanged");
                                equal(cents * 2, log.totalCents(), "sent total");
                                equal(Optional.of(quote), log.find(label), "modern lookup");
                                CompatibilityDesk adapter = new CompatibilityDesk(discount);
                                equal(cents, adapter.price(label, grams, zone, express, "unused"), "old client amount");
                                equal(legacy.history(), adapter.history(), "old client history");
                                error("UnsupportedOperationException:null", () -> earlier.add(quote));
                            }
                            rows++;
                        }
        LegacyDesk old = new LegacyDesk(0);
        old.price("seed", 1, 1, false, null);
        legacyInvalid(old);
        old.setDiscount(25);
        equal(115, old.price("seed", 1, 1, false, null), "future discount");
        equal(new Quote("seed", 115), old.find("seed"), "latest duplicate label");
        equal(255, old.totalCents(), "prior history retained");
        if (core) {
            DispatchLog log = new DispatchLog();
            DispatchService standard = DispatchService.standard(log);
            equal(new Quote("seed", 140), standard.send(new Parcel("seed", 1, 1), DeliverySpeed.STANDARD), "named standard factory");
            modernInvalid(standard, log);
            DispatchService discounted = DispatchService.discounted(25, log);
            equal(new Quote("seed", 115), discounted.send(new Parcel("seed", 1, 1), DeliverySpeed.STANDARD), "new configuration shares log");
            equal(Optional.of(new Quote("seed", 115)), log.find("seed"), "last matching quote");
            equal(255, log.totalCents(), "configuration history");
            equal(new Quote("X", 140), standard.quote(new Parcel("X", 1, 1), DeliverySpeed.STANDARD), "old configuration stays unchanged");
            CompatibilityDesk adapter = new CompatibilityDesk(0);
            adapter.price("A", 500, 1, false, null);
            List<Quote> first = adapter.history();
            adapter.setDiscount(25);
            equal(505, adapter.price("B", 501, 2, true, "unused"), "adapter setter");
            equal(645, adapter.totalCents(), "adapter preserves history");
            equal(List.of(new Quote("A", 140)), first, "adapter snapshot");
            List<Quote> before = adapter.history();
            equal(-1, adapter.price(null, 0, 0, false, null), "adapter sentinel");
            equal(before, adapter.history(), "adapter error no writes");
            equal(null, adapter.find(null), "adapter null lookup");
            equal(null, adapter.find(" "), "adapter blank lookup");
            equal(null, adapter.find("missing"), "adapter missing lookup");
            for (int grams : new int[] {0, 5001})
                equal(-1, adapter.price("X", grams, 1, false, null), "adapter bad grams");
            for (int zone : new int[] {0, 4})
                equal(-1, adapter.price("X", 1, zone, false, null), "adapter bad zone");
            error("IllegalArgumentException:discountCents", () -> adapter.setDiscount(-1));
            equal(115, adapter.price("C", 1, 1, false, null), "failed setter preserves config");
            equal("140\n505\n645\n-1\nQuote[label=B, cents=505]\nnull", ClientCompatibility.run(), "unchanged client");
        }
        System.out.println("mode=" + args[0] + " rows=" + rows + " checks=" + checks);
    }
}
