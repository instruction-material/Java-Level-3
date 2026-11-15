package api;
import java.util.*;
public record BoardStats(int tasks,int done,int remaining,List<String> openTitles) {
    public BoardStats {
        if(tasks<0||tasks>8)throw new IllegalArgumentException("tasks");
        if(done<0||done>tasks)throw new IllegalArgumentException("done");
        if(remaining<0||remaining>960)throw new IllegalArgumentException("remaining");
        openTitles=List.copyOf(Objects.requireNonNull(openTitles,"openTitles"));
        if(openTitles.size()!=tasks-done)throw new IllegalArgumentException("openTitles");
    }
}
