package roster;
import java.util.ArrayList;
import java.util.List;

/** Working candidate: replace duplicated concrete knowledge with Member. */
public final class Roster implements RosterAPI {
    private final List<String> lines = new ArrayList<>();
    private long total;
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
