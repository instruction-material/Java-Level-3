package roster;
/** Recompiled unchanged against each implementation. */
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static String run(RosterAPI roster) {
        roster.addVolunteer("Ada", 3);
        roster.addMentor("Bo", 4, 2);
        return String.join("\n", roster.lines()) + "\ntotal=" + roster.totalCredits() + "\n";
    }
}
