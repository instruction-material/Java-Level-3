package creation;
/** Products have stable non-null style metadata and accept non-null text. */
public interface Heading {
    Style style();
    String render(String title);
}
