import api.Documents;
import app.DocumentService;
import java.util.List;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        Documents client=new DocumentService();
        if(!client.build("plain",List.of("a")).equals("begin\na\nend\n"))throw new AssertionError("old workflow");
        System.out.println("compatibility passed");
    }
}
