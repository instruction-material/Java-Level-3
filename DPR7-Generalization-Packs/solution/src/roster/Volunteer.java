package roster;
public final class Volunteer extends Member {
    public Volunteer(String name, int sessions) { super(name, sessions); }
    protected String role() { return "volunteer"; }
    public int credits() { return sessionCredits(); }
}
