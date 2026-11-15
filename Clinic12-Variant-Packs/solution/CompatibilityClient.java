import api.Messages;
import app.MessageService;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        Messages client=new MessageService();
        if(!client.render("compact","go").equals("2|go\n"))throw new AssertionError("old client");
        System.out.println("compatibility passed");
    }
}
