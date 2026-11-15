package api;
import java.util.Objects;
public record Task(String title,int minutes,boolean done) {
    public Task {
        Objects.requireNonNull(title,"title");
        if(title.isBlank()||title.length()>16||title.indexOf(',')>=0||title.indexOf('|')>=0
            ||title.indexOf('\r')>=0||title.indexOf('\n')>=0)throw new IllegalArgumentException("title");
        if(minutes<0||minutes>120)throw new IllegalArgumentException("minutes");
    }
}
