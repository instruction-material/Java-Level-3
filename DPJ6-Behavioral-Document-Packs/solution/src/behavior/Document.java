// State: behavior and allowed transitions belong to the current private phase object.
package behavior;

public final class Document {
    private String text;
    private State phase = Phases.DRAFT;
    public Document(String text) { this.text = TextRule.line(text); }
    public String text() { return text; }
    public String phase() { return phase.name(); }
    public void replace(String value) { phase.replace(this, TextRule.line(value)); }
    public void submit() { phase.submit(this); }
    public void approve() { phase.approve(this); }
    public void revise() { phase.revise(this); }
    private interface State {
        String name();
        default void replace(Document document, String value) { throw new IllegalStateException("replace unavailable"); }
        default void submit(Document document) { throw new IllegalStateException("submit unavailable"); }
        default void approve(Document document) { throw new IllegalStateException("approve unavailable"); }
        default void revise(Document document) { throw new IllegalStateException("revise unavailable"); }
    }
    private enum Phases implements State {
        DRAFT {
            public void replace(Document document, String value) { document.text = value; }
            public void submit(Document document) { document.phase = REVIEW; }
        },
        REVIEW {
            public void approve(Document document) { document.phase = PUBLISHED; }
            public void revise(Document document) { document.phase = DRAFT; }
        },
        PUBLISHED
    }
}
