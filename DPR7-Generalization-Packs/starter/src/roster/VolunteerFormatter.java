package roster;
final class VolunteerFormatter extends RoleFormatter {
    String format(String name, int credits) { return name + " | volunteer | " + credits; }
}
