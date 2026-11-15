package roster;
import java.util.Objects;

/** Shared member data and validation; subclasses supply their genuine variation. */
public abstract class Member implements LineSource {
    private final String name;
    private final int sessions;
    protected Member(String name, int sessions) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) throw new IllegalArgumentException("name");
        if (sessions < 0 || sessions > 40) throw new IllegalArgumentException("sessions");
        this.name = name;
        this.sessions = sessions;
    }
    public final String name() { return name; }
    public final int sessions() { return sessions; }
    protected final int sessionCredits() { return sessions * 2; }
    protected abstract String role();
    public abstract int credits();
    public final String line() { return name + " | " + role() + " | " + credits(); }
}
