package notes.plugins;
/** Trusted, single-threaded extension. Ids follow Note.key; outputs follow Note.line. */
public interface TextPlugin {
    String id();
    String apply(String text);
}
