import notices.*;
import tickets.*;
import editor.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.function.Supplier;
public final class ContractChecks {
    private static long checks;
    private ContractChecks() {}
    private static void eq(Object expected, Object actual) {
        checks++; if (!Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    private static void yes(boolean value) { eq(true, value); }
    private static void fails(Class<? extends Throwable> type, String message, Runnable code) {
        try { code.run(); } catch (Throwable e) { eq(type, e.getClass()); if (message != null) eq(message, e.getMessage()); return; }
        throw new AssertionError("Expected " + type.getName());
    }
    private static Class<?> type(String name) {
        try { return Class.forName(name); } catch (ClassNotFoundException e) { throw new AssertionError("Unfinished extraction: " + name, e); }
    }
    private static void notices(NoticeAPI candidate) {
        NoticeAPI before = new LegacyNotices();
        eq("Notes\n[kit:3]\n[lamp:2]\nend=2\n", candidate.render("bracket", List.of(new Notice("lamp", 2), new Notice("kit", 3)), true));
        for (int a = 0; a <= 9; a++) for (int b = 0; b <= 9; b++) {
            for (String label : List.of("lamp", " A,B ", "[x]:=", "é")) {
                List<Notice> input = new ArrayList<>(List.of(new Notice(label, a), new Notice("kit", b)));
                List<Notice> copy = List.copyOf(input);
                for (String style : List.of("text", "bracket")) for (boolean reverse : List.of(false, true))
                    eq(before.render(style, input, reverse), candidate.render(style, input, reverse));
                eq(copy, input);
            }
        }
        for (String style : List.of("text", "bracket")) eq("Notes\nend=0\n", candidate.render(style, List.of(), true));
        fails(NullPointerException.class, "style", () -> candidate.render(null, null, false));
        fails(IllegalArgumentException.class, "style", () -> candidate.render("TEXT", null, false));
        fails(NullPointerException.class, "notices", () -> candidate.render("text", null, false));
        fails(NullPointerException.class, "notice", () -> candidate.render("text", Arrays.asList((Notice)null), false));
        fails(NullPointerException.class, "label", () -> new Notice(null, -1));
        for (String label : List.of("", " ", "a\nb", "a\rb")) fails(IllegalArgumentException.class, "label", () -> new Notice(label, -1));
        for (int count : List.of(-1, 10, Integer.MIN_VALUE, Integer.MAX_VALUE)) fails(IllegalArgumentException.class, "count", () -> new Notice("ok", count));
    }
    private static void ticketSequence(Supplier<TicketAPI> factory, List<String> sequence) {
        TicketAPI before = new LegacyTicket(), candidate = factory.get();
        List<String> empty = candidate.history();
        eq("DRAFT", candidate.status());
        for (String action : sequence) {
            eq(before.apply(action), candidate.apply(action)); eq(before.status(), candidate.status()); eq(before.history(), candidate.history());
        }
        eq(List.of(), empty);
        List<String> prior = candidate.history(); String state = candidate.status();
        fails(NullPointerException.class, "action", () -> candidate.apply(null));
        fails(IllegalArgumentException.class, "action", () -> candidate.apply("APPROVE"));
        eq(state, candidate.status()); eq(prior, candidate.history());
        fails(UnsupportedOperationException.class, null, () -> candidate.history().add("corrupt"));
    }
    private static void tickets(Supplier<TicketAPI> factory) {
        for (int length = 0; length <= 5; length++) {
            int combinations = (int)Math.pow(3, length);
            for (int n = 0; n < combinations; n++) {
                List<String> sequence = new ArrayList<>(); int code = n;
                for (int i = 0; i < length; i++) { sequence.add(List.of("approve", "finish", "reset").get(code % 3)); code /= 3; }
                ticketSequence(factory, sequence);
            }
        }
    }
    private static void editors(Supplier<EditorAPI> factory) {
        for (int length = 0; length <= 5; length++) {
            int combinations = (int)Math.pow(4, length);
            for (int n = 0; n < combinations; n++) {
                EditorAPI before = new LegacyEditor(), candidate = factory.get(); int code = n;
                for (int i = 0; i < length; i++) {
                    int op = code % 4; code /= 4;
                    if (op < 2) { String value = op == 0 ? " a " : "é\nnext"; before.append(2024, 2, 29, value); candidate.append(2024, 2, 29, value); }
                    else if (op == 2) eq(before.undo(), candidate.undo());
                    else eq(before.redo(), candidate.redo());
                    eq(before.text(), candidate.text());
                }
            }
        }
        EditorAPI editor = factory.get();
        editor.append(2026, 11, 15, "a"); editor.append(2026, 11, 16, "b"); yes(editor.undo());
        String saved = editor.text();
        fails(IllegalArgumentException.class, "year", () -> editor.append(999, 13, 99, null));
        fails(java.time.DateTimeException.class, null, () -> editor.append(2025, 2, 29, null));
        fails(NullPointerException.class, "text", () -> editor.append(2026, 1, 1, null));
        fails(IllegalArgumentException.class, "text", () -> editor.append(2026, 1, 1, ""));
        eq(saved, editor.text()); yes(editor.redo()); yes(editor.undo());
        editor.append(2026, 11, 17, "c"); eq(false, editor.redo());
        eq("2026-11-15 | a\n2026-11-17 | c\n", editor.text());
        yes(editor.undo()); yes(editor.undo()); eq(false, editor.undo()); eq("", editor.text());
        yes(editor.redo()); yes(editor.redo()); eq(false, editor.redo());
        for (int[] date : List.of(new int[]{1000,1,1}, new int[]{9999,12,31}, new int[]{2000,2,29})) {
            EditorAPI b = new LegacyEditor(), c = factory.get(); b.append(date[0], date[1], date[2], "x"); c.append(date[0], date[1], date[2], "x"); eq(b.text(), c.text());
        }
    }
    private static void coreStructure() throws ReflectiveOperationException {
        Class<?> creator = type("notices.NoticeCreator"), product = type("notices.NoticeLine"), order = type("notices.Ordering");
        yes(Modifier.isAbstract(creator.getModifiers())); yes(product.isInterface()); yes(order.isInterface());
        yes(Modifier.isFinal(creator.getMethod("render", List.class, order).getModifiers()));
        Method hook = creator.getDeclaredMethod("createLine"); yes(Modifier.isAbstract(hook.getModifiers())); eq(product, hook.getReturnType());
        for (String name : List.of("TextCreator", "BracketCreator")) eq(creator, type("notices." + name).getSuperclass());
        for (String name : List.of("OriginalOrder", "ReversedOrder")) yes(order.isAssignableFrom(type("notices." + name)));
    }
    private static void transferStructure() {
        Class<?> state = type("tickets.TicketState"); yes(state.isEnum());
        yes(Arrays.stream(Ticket.class.getDeclaredFields()).anyMatch(f -> f.getType() == state && Modifier.isPrivate(f.getModifiers())));
    }
    private static void extensionStructure() throws ReflectiveOperationException {
        Class<?> buffer = type("editor.TextBuffer"), memory = type("editor.TextBuffer$Memento");
        yes(Modifier.isFinal(memory.getModifiers()));
        for (Field field : memory.getDeclaredFields()) { yes(Modifier.isPrivate(field.getModifiers())); yes(Modifier.isFinal(field.getModifiers())); }
        for (Constructor<?> constructor : memory.getDeclaredConstructors()) yes(Modifier.isPrivate(constructor.getModifiers()));
        yes(type("editor.NoteDate").isAssignableFrom(type("editor.DayAdapter")));
        yes(type("editor.EditCommand").isAssignableFrom(type("editor.AppendCommand")));
        Object one = buffer.getConstructor().newInstance(), two = buffer.getConstructor().newInstance();
        Object saved = buffer.getMethod("save").invoke(one);
        try { buffer.getMethod("restore", memory).invoke(two, saved); throw new AssertionError("cross-owner restore accepted"); }
        catch (InvocationTargetException e) { eq(IllegalArgumentException.class, e.getCause().getClass()); eq("owner", e.getCause().getMessage()); }
        Class<?> extension = type("editor.DayExtension");
        for (int[] date : List.of(new int[]{2026,11,14}, new int[]{2026,11,15}, new int[]{2026,11,16})) {
            Object day = extension.getConstructor(AncientDay.class).newInstance(new AncientDay(date[0], date[1], date[2]));
            eq(date[2] != 16, extension.getMethod("weekend").invoke(day));
        }
    }
    public static void main(String[] args) throws ReflectiveOperationException {
        String mode = args.length == 0 ? "full" : args[0];
        switch (mode) {
            case "baseline" -> { notices(new NoticeService()); tickets(Ticket::new); editors(Editor::new); }
            case "core" -> { notices(new NoticeService()); coreStructure(); }
            case "transfer" -> { tickets(Ticket::new); tickets(GuardTicket::new); tickets(StrategyTicket::standard); transferStructure(); }
            case "extension" -> { editors(Editor::new); extensionStructure(); }
            case "full" -> { notices(new NoticeService()); coreStructure(); tickets(Ticket::new); tickets(GuardTicket::new); tickets(StrategyTicket::standard); transferStructure(); editors(Editor::new); extensionStructure(); }
            default -> throw new IllegalArgumentException("mode");
        }
        eq("Notes\nlamp=2\nend=1\nREADY\n2026-11-15 | lamp\n", CompatibilityClient.run(new NoticeService(), new Ticket(), new Editor()));
        System.out.println(mode + ": " + checks + " checks passed");
    }
}
