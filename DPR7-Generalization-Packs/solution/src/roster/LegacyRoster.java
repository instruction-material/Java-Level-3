package roster;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Frozen working baseline. Its duplicate classes are deliberately retained. */
public final class LegacyRoster implements RosterAPI {
    private final List<String> lines = new ArrayList<>();
    private long total;
    private abstract static class Formatter { abstract String format(String name, int credits); }
    private static final class VolunteerFormatter extends Formatter {
        String format(String name, int credits) { return name + " | volunteer | " + credits; }
    }
    private static final class MentorFormatter extends Formatter {
        String format(String name, int credits) { return name + " | mentor | " + credits; }
    }
    private static final class Volunteer {
        private final Formatter formatter = new VolunteerFormatter();
        private final String name;
        private final int sessions;
        Volunteer(String name, int sessions) {
            Objects.requireNonNull(name, "name");
            if (name.isBlank()) throw new IllegalArgumentException("name");
            if (sessions < 0 || sessions > 40) throw new IllegalArgumentException("sessions");
            this.name = name;
            this.sessions = sessions;
        }
        int credits() { return sessions * 2; }
        String line() { return formatter.format(name, credits()); }
    }
    private static final class Mentor {
        private final Formatter formatter = new MentorFormatter();
        private final String name;
        private final int sessions;
        private final int mentoringSlots;
        Mentor(String name, int sessions, int mentoringSlots) {
            Objects.requireNonNull(name, "name");
            if (name.isBlank()) throw new IllegalArgumentException("name");
            if (sessions < 0 || sessions > 40) throw new IllegalArgumentException("sessions");
            if (mentoringSlots < 0 || mentoringSlots > 8) throw new IllegalArgumentException("mentoringSlots");
            this.name = name;
            this.sessions = sessions;
            this.mentoringSlots = mentoringSlots;
        }
        int credits() { return sessions * 2 + mentoringSlots * 3; }
        String line() { return formatter.format(name, credits()); }
    }
    public void addVolunteer(String name, int sessions) {
        Volunteer member = new Volunteer(name, sessions);
        lines.add(member.line());
        total += member.credits();
    }
    public void addMentor(String name, int sessions, int slots) {
        Mentor member = new Mentor(name, sessions, slots);
        lines.add(member.line());
        total += member.credits();
    }
    public List<String> lines() { return List.copyOf(lines); }
    public long totalCredits() { return total; }
}
