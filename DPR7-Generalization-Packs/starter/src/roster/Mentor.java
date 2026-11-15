package roster;
import java.util.Objects;

/** Only the mentoring-slot responsibility belongs in this subtype. */
public final class Mentor {
    private final String name;
    private final int sessions;
    private final int mentoringSlots;
    public Mentor(String name, int sessions, int mentoringSlots) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) throw new IllegalArgumentException("name");
        if (sessions < 0 || sessions > 40) throw new IllegalArgumentException("sessions");
        if (mentoringSlots < 0 || mentoringSlots > 8) throw new IllegalArgumentException("mentoringSlots");
        this.name = name;
        this.sessions = sessions;
        this.mentoringSlots = mentoringSlots;
    }
    public String name() { return name; }
    public int sessions() { return sessions; }
    public int credits() { return sessions * 2 + mentoringSlots * 3; }
    public String line() { return name + " | mentor | " + credits(); }
}
