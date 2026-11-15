package roster;
import java.util.List;

/** The supplied client contract stays unchanged while internal types change. */
public interface RosterAPI {
    void addVolunteer(String name, int sessions);
    void addMentor(String name, int sessions, int mentoringSlots);
    List<String> lines();
    long totalCredits();
}
