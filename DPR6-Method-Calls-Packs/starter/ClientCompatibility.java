import dispatch.CompatibilityDesk;

/** Existing source client: preserve all calls and the six output lines. */
public final class ClientCompatibility {
    private ClientCompatibility() {}
    public static String run() {
        CompatibilityDesk desk = new CompatibilityDesk(0);
        StringBuilder output = new StringBuilder();
        output.append(desk.price("A", 500, 1, false, null)).append("\n");
        desk.setDiscount(25);
        output.append(desk.price("B", 501, 2, true, "ignored")).append("\n");
        output.append(desk.totalCents()).append("\n");
        output.append(desk.price(null, 0, 0, false, null)).append("\n");
        output.append(desk.find("B")).append("\n");
        output.append(desk.find("missing"));
        return output.toString();
    }
}
