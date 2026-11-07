public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        if (!actual.equals(expected)) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Wrong failure", error);
            checks++; return;
        }
        throw new AssertionError("Missing " + type.getSimpleName());
    }
    public static void main(String[] args) {
        for (String text : new String[]{"Ready", "", "One\nTwo", "i", "🙂"})
            equal(Main.demonstration(text), text);
        equal(new Main.Message("Ready"), new Main.Message("Ready"));
        int[] calls = {0};
        String[] received = {""};
        Main.Sender sender = new Main.Sender(text -> { calls[0]++; received[0] = text; });
        sender.send(new Main.Message("Ready"));
        equal(calls[0], 1);
        equal(received[0], "Ready");
        fails(NullPointerException.class, () -> new Main.Message(null));
        fails(NullPointerException.class, () -> new Main.Sender(null));
        fails(NullPointerException.class, () -> sender.send(null));
        equal(calls[0], 1);
        fails(NullPointerException.class, () -> Main.demonstration(null));
        fails(IllegalStateException.class, () -> new Main.Sender(text -> { throw new IllegalStateException("write failed"); }).send(new Main.Message("Ready")));
        System.out.println("Seam worked contracts: " + checks + " passed");
    }
}
