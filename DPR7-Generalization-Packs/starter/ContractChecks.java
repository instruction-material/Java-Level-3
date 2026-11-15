import roster.*;
import reports.*;
import events.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/** Explicit checks work with both enabled and disabled Java assertions. */
public final class ContractChecks {
    private static int checks;
    private ContractChecks() {}
    private static void check(boolean ok, String message) {
        checks++;
        if (!ok) throw new AssertionError(message);
    }
    private static void equal(Object expected, Object actual) {
        check(Objects.equals(expected, actual), "expected=" + expected + " actual=" + actual);
    }
    private static void rejects(Class<? extends Throwable> type, String message, Runnable action) {
        Throwable caught = null;
        try { action.run(); } catch (Throwable failure) { caught = failure; }
        check(caught != null && type.isInstance(caught), "Expected " + type.getSimpleName() + ", got " + caught);
        if (message != null) equal(message, caught.getMessage());
    }
    private static Class<?> required(String name) {
        try { return Class.forName(name); }
        catch (ClassNotFoundException missing) {
            throw new AssertionError("Missing refactoring class: " + name, missing);
        }
    }
    private static void roster(Supplier<RosterAPI> create) {
        for (String name : List.of("Ada", " Bo ", "\u00e9")) {
            for (int sessions = 0; sessions <= 40; sessions++) {
                RosterAPI r = create.get();
                List<String> empty = r.lines();
                r.addVolunteer(name, sessions);
                equal(List.of(name + " | volunteer | " + (sessions * 2)), r.lines());
                equal((long) sessions * 2, r.totalCredits());
                equal(List.of(), empty);
                List<String> volunteerSnapshot = r.lines();
                rejects(UnsupportedOperationException.class, null, () -> volunteerSnapshot.add("bad"));
                for (int slots = 0; slots <= 8; slots++) {
                    r = create.get();
                    r.addMentor(name, sessions, slots);
                    int expected = sessions * 2 + slots * 3;
                    equal(List.of(name + " | mentor | " + expected), r.lines());
                    equal((long) expected, r.totalCredits());
                }
            }
        }
        RosterAPI r = create.get();
        r.addVolunteer("Ada", 3);
        List<String> prior = r.lines();
        r.addMentor("Bo", 4, 2);
        equal(List.of("Ada | volunteer | 6"), prior);
        equal(List.of("Ada | volunteer | 6", "Bo | mentor | 14"), r.lines());
        equal(20L, r.totalCredits());
        List<String> before = r.lines();
        rejects(NullPointerException.class, "name", () -> r.addMentor(null, -1, -1));
        rejects(IllegalArgumentException.class, "name", () -> r.addMentor(" ", -1, -1));
        rejects(IllegalArgumentException.class, "sessions", () -> r.addMentor("A", -1, -1));
        rejects(IllegalArgumentException.class, "sessions", () -> r.addVolunteer("A", 41));
        rejects(IllegalArgumentException.class, "mentoringSlots", () -> r.addMentor("A", 1, 9));
        equal(before, r.lines()); equal(20L, r.totalCredits());
        equal("Ada | volunteer | 6\nBo | mentor | 14\ntotal=20\n", CompatibilityClient.run(create.get()));
        equal("Supported by North Lab", new SponsorBanner("North Lab").line());
    }
    private static void rosterShape() throws ReflectiveOperationException {
        Class<?> member = required("roster.Member");
        Class<?> line = required("roster.LineSource");
        check(Modifier.isAbstract(member.getModifiers()), "Member must represent common member responsibilities");
        check(line.isInterface(), "LineSource must be a capability interface");
        check(member.isAssignableFrom(Volunteer.class) && member.isAssignableFrom(Mentor.class), "Extract shared Member superclass");
        check(line.isAssignableFrom(member) && line.isAssignableFrom(SponsorBanner.class), "Unrelated line suppliers share LineSource");
        check(!member.isAssignableFrom(SponsorBanner.class), "A sponsor is not a roster member");
        member.getDeclaredField("name"); member.getDeclaredField("sessions");
        for (Class<?> subtype : List.of(Volunteer.class, Mentor.class)) {
            check(Arrays.stream(subtype.getDeclaredFields()).noneMatch(f -> f.getName().equals("name") || f.getName().equals("sessions")), "Move duplicate member fields up");
            equal(member, subtype.getMethod("name").getDeclaringClass());
            equal(member, subtype.getMethod("sessions").getDeclaringClass());
            equal(member, subtype.getMethod("line").getDeclaringClass());
        }
        member.getDeclaredMethod("sessionCredits");
        for (String old : List.of("roster.RoleFormatter", "roster.VolunteerFormatter", "roster.MentorFormatter")) {
            try { Class.forName(old); throw new AssertionError("Remove parallel formatter: " + old); }
            catch (ClassNotFoundException removed) { checks++; }
        }
    }
    @FunctionalInterface private interface Render {
        String apply(Format format, List<Row> rows);
    }
    private static void reports(Render render) {
        List<Row> worked = List.of(new Row("tool, kit", 2), new Row("lamp", 3));
        equal("Inventory\ntool, kit=2\nlamp=3\nitems=5\n", render.apply(Format.PLAIN, worked));
        equal("name,count\n\"tool, kit\",2\n\"lamp\",3\ntotal,5\n", render.apply(Format.CSV, worked));
        equal("name,count\n\"say \"\"hi\"\"\",1\ntotal,1\n", render.apply(Format.CSV, List.of(new Row("say \"hi\"", 1))));
        equal("name,count\n\"a\nb\",0\ntotal,0\n", render.apply(Format.CSV, List.of(new Row("a\nb", 0))));
        for (String label : List.of("plain", "tool, kit", "say \"hi\"", "a\nb", " \u00e9 ")) {
            for (int count : List.of(0, 1, 49, 50)) {
                List<Row> rows = new ArrayList<>(List.of(new Row(label, count), new Row("second", 50 - count)));
                List<Row> before = List.copyOf(rows);
                for (Format format : Format.values()) {
                    equal(LegacyReports.render(format, rows), render.apply(format, rows));
                    equal(before, rows);
                    equal(LegacyReports.render(format, List.of()), render.apply(format, List.of()));
                }
            }
        }
        rejects(NullPointerException.class, "format", () -> render.apply(null, null));
        rejects(NullPointerException.class, "rows", () -> render.apply(Format.PLAIN, null));
        rejects(NullPointerException.class, "row", () -> render.apply(Format.CSV, Arrays.asList(new Row("A", 1), null)));
        rejects(NullPointerException.class, "label", () -> new Row(null, -1));
        rejects(IllegalArgumentException.class, "label", () -> new Row(" ", -1));
        rejects(IllegalArgumentException.class, "count", () -> new Row("A", -1));
        rejects(IllegalArgumentException.class, "count", () -> new Row("A", 51));
    }
    private static void reportShape() throws ReflectiveOperationException {
        Class<?> report = required("reports.Report");
        Class<?> plain = required("reports.PlainReport");
        Class<?> csv = required("reports.CsvReport");
        check(Modifier.isAbstract(report.getModifiers()), "Extract the common report algorithm");
        check(Modifier.isFinal(report.getMethod("render", List.class).getModifiers()), "Keep the template order in Report.render");
        equal(report, plain.getSuperclass()); equal(report, csv.getSuperclass());
        check(Arrays.stream(report.getDeclaredFields()).noneMatch(f -> f.getName().equals("delimiter")), "CSV delimiter belongs in CsvReport");
        check(Arrays.stream(report.getDeclaredMethods()).noneMatch(m -> m.getName().equals("quoteLabel")), "CSV quoting belongs in CsvReport");
        csv.getDeclaredField("delimiter"); csv.getDeclaredMethod("quoteLabel", String.class);
        for (String hook : List.of("header", "footer", "rowText")) {
            check(Arrays.stream(report.getDeclaredMethods()).anyMatch(m -> m.getName().equals(hook) && Modifier.isAbstract(m.getModifiers())), "Supply the " + hook + " format hook");
        }
    }
    private static void events(IntFunction<EventBufferAPI> create, Function<String, NamedValue> named) {
        for (int capacity = 1; capacity <= 8; capacity++) {
            for (int count = 0; count <= 20; count++) {
                EventBufferAPI buffer = create.apply(capacity);
                List<String> empty = buffer.snapshot();
                for (int i = 0; i < count; i++) buffer.push("E" + i);
                List<String> expected = new ArrayList<>();
                for (int i = Math.max(0, count - capacity); i < count; i++) expected.add("E" + i);
                equal(expected, buffer.snapshot()); equal(List.of(), empty);
                List<String> snapshot = buffer.snapshot();
                rejects(UnsupportedOperationException.class, null, () -> snapshot.add("bad"));
                rejects(NullPointerException.class, "event", () -> buffer.push(null));
                rejects(IllegalArgumentException.class, "event", () -> buffer.push(" "));
                equal(expected, buffer.snapshot());
                for (String value : expected) equal(value, buffer.next());
                equal(List.of(), buffer.snapshot()); equal(expected, snapshot);
                rejects(java.util.NoSuchElementException.class, "empty", buffer::next);
            }
        }
        rejects(IllegalArgumentException.class, "capacity", () -> create.apply(0));
        rejects(IllegalArgumentException.class, "capacity", () -> create.apply(9));
        EventBufferAPI buffer = create.apply(2);
        buffer.push("A"); buffer.push("B");
        List<String> prior = buffer.snapshot();
        equal("A", buffer.next()); buffer.push(" C ");
        equal(List.of("B", " C "), buffer.snapshot()); equal(List.of("A", "B"), prior);
        for (String label : List.of("visits", " x ", "\u00e9")) {
            NamedValue counter = named.apply(label);
            equal(label, counter.label()); equal(0L, counter.value());
            for (int i = 0; i <= 100; i++) {
                counter.add(i);
                equal((long) i * (i + 1) / 2, counter.value());
                equal(label, counter.label());
            }
            rejects(IllegalArgumentException.class, "amount", () -> counter.add(-1));
            equal(5050L, counter.value());
            counter.reset(); equal(0L, counter.value());
            counter.add(Long.MAX_VALUE); equal(Long.MAX_VALUE, counter.value());
            rejects(ArithmeticException.class, null, () -> counter.add(1));
            equal(Long.MAX_VALUE, counter.value());
            counter.reset(); equal(0L, counter.value()); equal(label, counter.label());
        }
        rejects(NullPointerException.class, "label", () -> named.apply(null));
        rejects(IllegalArgumentException.class, "label", () -> named.apply(" "));
    }
    private static void eventShape() {
        check(!List.class.isAssignableFrom(EventBuffer.class), "EventBuffer still exposes List");
        try { Class.forName("events.DefaultEventBuffer"); throw new AssertionError("Collapse empty DefaultEventBuffer"); }
        catch (ClassNotFoundException removed) { checks++; }
        check(Counter.class.isAssignableFrom(NamedCounter.class), "NamedCounter must inherit the unchanged Counter contract");
        check(Arrays.stream(NamedCounter.class.getDeclaredFields()).noneMatch(f -> f.getType().equals(Counter.class)), "Remove the redundant Counter delegate");
    }
    public static void main(String[] args) throws ReflectiveOperationException {
        String mode = args.length == 0 ? "baseline" : args[0];
        switch (mode) {
            case "baseline" -> {
                roster(LegacyRoster::new);
                reports(LegacyReports::render);
                events(LegacyEventModels::buffer, LegacyEventModels::named);
            }
            case "core" -> { roster(Roster::new); rosterShape(); }
            case "transfer" -> { reports(ReportService::render); reportShape(); }
            case "extension" -> { events(EventFactory::create, NamedCounter::new); eventShape(); }
            case "full" -> {
                roster(Roster::new); rosterShape();
                reports(ReportService::render); reportShape();
                events(EventFactory::create, NamedCounter::new); eventShape();
            }
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println("mode=" + mode + " checks=" + checks);
    }
}
