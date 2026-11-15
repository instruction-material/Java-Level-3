package roster;
import java.util.ArrayList;
import java.util.List;
public final class Roster implements RosterAPI {
    private final List<Member> members = new ArrayList<>();
    public void addVolunteer(String name, int sessions) {
        members.add(new Volunteer(name, sessions));
    }
    public void addMentor(String name, int sessions, int slots) {
        members.add(new Mentor(name, sessions, slots));
    }
    public List<String> lines() { return members.stream().map(Member::line).toList(); }
    public long totalCredits() { return members.stream().mapToLong(Member::credits).sum(); }
}
