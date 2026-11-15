import api.*;
import app.WorkshopDesk;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        DeskAPI desk = new WorkshopDesk();
        desk.open("room", 1);
        if (desk.join("room", "ana") != JoinStatus.ENROLLED) throw new AssertionError("join");
        if (!desk.report("compact").equals("1\nroom|1|open|ana|-\n")) throw new AssertionError("report");
        System.out.println("compatibility passed");
    }
}
