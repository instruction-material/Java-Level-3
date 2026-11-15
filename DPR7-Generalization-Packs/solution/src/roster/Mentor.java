package roster;
public final class Mentor extends Member {
    private final int mentoringSlots;
    public Mentor(String name, int sessions, int mentoringSlots) {
        super(name, sessions);
        if (mentoringSlots < 0 || mentoringSlots > 8) throw new IllegalArgumentException("mentoringSlots");
        this.mentoringSlots = mentoringSlots;
    }
    protected String role() { return "mentor"; }
    public int credits() { return sessionCredits() + mentoringSlots * 3; }
}
