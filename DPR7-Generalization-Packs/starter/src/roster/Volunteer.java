package roster;
import java.util.Objects;

/** Refactor the shared fields, constructor work and methods into Member. */
public final class Volunteer {
    private final RoleFormatter formatter = new VolunteerFormatter();
    private final String name;
    private final int sessions;
    public Volunteer(String name, int sessions) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) throw new IllegalArgumentException("name");
        if (sessions < 0 || sessions > 40) throw new IllegalArgumentException("sessions");
        this.name = name;
        this.sessions = sessions;
    }
    public String name() { return name; }
    public int sessions() { return sessions; }
    public int credits() { return sessions * 2; }
    public String line() { return formatter.format(name, credits()); }
}
