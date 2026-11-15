package api;
import java.util.Objects;
public record Profile(String name,String channel) {
    public Profile {
        Objects.requireNonNull(name,"name");
        if(name.isBlank()||name.length()>16||name.indexOf('|')>=0||name.indexOf('\r')>=0||name.indexOf('\n')>=0)
            throw new IllegalArgumentException("name");
        Objects.requireNonNull(channel,"channel");
        if(!channel.equals("email")&&!channel.equals("screen"))throw new IllegalArgumentException("channel");
    }
}
