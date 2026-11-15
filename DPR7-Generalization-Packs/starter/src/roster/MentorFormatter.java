package roster;
final class MentorFormatter extends RoleFormatter {
    String format(String name, int credits) { return name + " | mentor | " + credits; }
}
