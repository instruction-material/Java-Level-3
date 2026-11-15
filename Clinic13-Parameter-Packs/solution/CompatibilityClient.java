import api.Ranges;
import app.RangeService;
import java.util.List;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        Ranges client=new RangeService();
        if(!client.values(2,9,3).equals(List.of(2,5,8)))throw new AssertionError("old range call");
        System.out.println("compatibility passed");
    }
}
