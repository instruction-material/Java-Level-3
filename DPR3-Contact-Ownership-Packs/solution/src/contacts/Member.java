package contacts;

import java.util.Objects;

public final class Member {
    private final String name;
    private Phone phone;
    public Member(String name, String area, String number) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || name.codePointCount(0, name.length()) > 60
                || name.indexOf('\r') >= 0 || name.indexOf('\n') >= 0
                || name.indexOf('<') >= 0 || name.indexOf('>') >= 0)
            throw new IllegalArgumentException("name");
        this.name = name;
        this.phone = new Phone(area, number);
    }
    public String displayName() { return name; }
    public String areaCode() { return phone.areaCode(); }
    public String localNumber() { return phone.localNumber(); }
    public String telephone() { return phone.format(); }
    public void changePhone(String nextArea, String nextNumber) {
        phone = new Phone(nextArea, nextNumber);
    }
    public String maskedTelephone() { return phone.masked(); }
}
