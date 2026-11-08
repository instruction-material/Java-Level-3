import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import wrapping.ReadDesk;
import wrapping.Source;
import wrapping.legacy.LegacyReader;

public final class ContractTests {
    private static int checks;
    private interface Api {
        String read(String key);
        long attempts();
        void clearCache();
    }
    @FunctionalInterface
    private interface Factory {
        Api create(Source source, String prefix, boolean upper, boolean cached);
    }
    private static final class Fake implements Source {
        final Map<String, String> texts = new HashMap<>(Map.of("a", "Hi", "b", "Straße", "c", ""));
        final Map<String, Integer> loads = new HashMap<>();
        RuntimeException failure;
        @Override
        public String load(String key) {
            loads.merge(key, 1, Integer::sum);
            if (failure != null) throw failure;
            return texts.get(key);
        }
        int count(String key) { return loads.getOrDefault(key, 0); }
    }
    private static void equal(Object actual, Object expected) {
        checks++;
        if (!Objects.equals(actual, expected))
            throw new AssertionError("Expected " + expected + ", got " + actual);
    }
    private static void rejects(Class<? extends Throwable> expected, Runnable task) {
        checks++;
        try { task.run(); }
        catch (Throwable error) {
            if (expected.isInstance(error)) return;
            throw new AssertionError("Wrong failure type", error);
        }
        throw new AssertionError("Expected " + expected.getName());
    }
    private static void sameFailure(Api api, RuntimeException original) {
        try {
            api.read("a");
            throw new AssertionError("Source failure was lost");
        } catch (RuntimeException actual) { equal(actual, original); }
    }
    private static void core(Factory factory) {
        for (boolean upper : new boolean[] {false, true}) {
            for (boolean cached : new boolean[] {false, true}) {
                for (String prefix : new String[] {"", "tag:", "pre-"}) {
                    Fake fake = new Fake();
                    Api api = factory.create(fake, prefix, upper, cached);
                    String[] keys = {"a", "b", "c"};
                    String[] expected = upper ? new String[] {"HI", "STRASSE", ""} : new String[] {"Hi", "Straße", ""};
                    equal(api.attempts(), 0L);
                    for (int pass = 0; pass < 2; pass++)
                        for (int i = 0; i < keys.length; i++) equal(api.read(keys[i]), prefix + expected[i]);
                    equal(api.attempts(), 6L);
                    for (String key : keys) equal(fake.count(key), cached ? 1 : 2);
                    fake.texts.put("a", "Bye");
                    equal(api.read("a"), prefix + (cached ? expected[0] : upper ? "BYE" : "Bye"));
                    api.clearCache();
                    equal(api.attempts(), 7L);
                    equal(api.read("a"), prefix + (upper ? "BYE" : "Bye"));
                    equal(fake.count("a"), cached ? 2 : 4);
                    equal(api.attempts(), 8L);
                    for (String invalid : new String[] {"", "A", "bad key", "a/b", "a".repeat(17)})
                        rejects(IllegalArgumentException.class, () -> api.read(invalid));
                    rejects(NullPointerException.class, () -> api.read(null));
                    equal(api.attempts(), 8L);
                    fake.texts.put("a".repeat(16), "ok");
                    equal(api.read("a".repeat(16)), prefix + (upper ? "OK" : "ok"));

                    Fake failures = new Fake();
                    RuntimeException original = new IllegalStateException("lookup failure");
                    failures.failure = original;
                    Api failed = factory.create(failures, prefix, upper, cached);
                    sameFailure(failed, original);
                    sameFailure(failed, original);
                    equal(failures.count("a"), 2);
                    equal(failed.attempts(), 2L);
                    failures.failure = null;
                    equal(failed.read("a"), prefix + expected[0]);
                    equal(failures.count("a"), 3);
                    failures.texts.remove("b");
                    rejects(NullPointerException.class, () -> failed.read("b"));
                    failures.texts.put("b", "new");
                    equal(failed.read("b"), prefix + (upper ? "NEW" : "new"));
                    equal(failures.count("b"), 2);
                    equal(failed.attempts(), 5L);
                }
            }
        }
        rejects(NullPointerException.class, () -> factory.create(null, "", false, false));
        rejects(NullPointerException.class, () -> factory.create(new Fake(), null, false, false));
    }
    private static void optional() {
        Fake fake = new Fake();
        ReadDesk api = new ReadDesk(fake, "", false, true);
        equal(api.read("a"), "Hi");
        equal(api.read("b"), "Straße");
        equal(api.read("c"), "");
        equal(api.invalidate("a"), true);
        equal(api.invalidate("a"), false);
        equal(api.invalidate("c"), true);
        equal(api.invalidate("missing"), false);
        equal(api.attempts(), 3L);
        equal(fake.count("a"), 1);
        fake.texts.put("a", "changed");
        fake.texts.put("b", "stale");
        equal(api.read("a"), "changed");
        equal(api.read("b"), "Straße");
        equal(fake.count("a"), 2);
        equal(fake.count("b"), 1);
        equal(api.attempts(), 5L);
        rejects(IllegalArgumentException.class, () -> api.invalidate("bad key"));
        rejects(NullPointerException.class, () -> api.invalidate(null));
        equal(api.attempts(), 5L);
        ReadDesk uncached = new ReadDesk(fake, "", false, false);
        equal(uncached.invalidate("a"), false);
        equal(uncached.attempts(), 0L);
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!mode.equals("base") && !mode.equals("core") && !mode.equals("all"))
            throw new IllegalArgumentException("mode");
        core((source, prefix, upper, cached) -> {
            LegacyReader desk = new LegacyReader(source, prefix, upper, cached);
            return new Api() {
                public String read(String key) { return desk.read(key); }
                public long attempts() { return desk.attempts(); }
                public void clearCache() { desk.clearCache(); }
            };
        });
        if (!mode.equals("base")) {
            core((source, prefix, upper, cached) -> {
                ReadDesk desk = new ReadDesk(source, prefix, upper, cached);
                return new Api() {
                    public String read(String key) { return desk.read(key); }
                    public long attempts() { return desk.attempts(); }
                    public void clearCache() { desk.clearCache(); }
                };
            });
            if (mode.equals("all")) optional();
        }
        System.out.println((mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core" : "Complete")
            + " wrapper studio contracts: " + checks + " passed");
    }
}
