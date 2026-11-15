package roster;
/** A read-only text capability shared by otherwise unrelated objects. */
public interface LineSource {
    String line();
}
