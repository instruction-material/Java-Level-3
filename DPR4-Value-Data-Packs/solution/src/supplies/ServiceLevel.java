package supplies;

/** Finite type codes and their own dispatch rule. */
public enum ServiceLevel {
    STANDARD(0, 180), RUSH(1, 60), EXPRESS(2, 30);
    private final int code;
    private final int minutes;
    ServiceLevel(int code, int minutes) { this.code = code; this.minutes = minutes; }
    public int code() { return code; }
    public int dispatchMinutes() { return minutes; }
    public static ServiceLevel fromCode(int code) {
        return switch (code) {
            case 0 -> STANDARD;
            case 1 -> RUSH;
            case 2 -> EXPRESS;
            default -> throw new IllegalArgumentException("service");
        };
    }
}
