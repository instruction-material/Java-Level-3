package creation;
/** A separate product type; its style must match the chosen heading. */
public interface BodyLine {
    Style style();
    String render(String text);
}
