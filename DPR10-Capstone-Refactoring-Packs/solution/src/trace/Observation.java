package trace;
import api.*;
import java.util.List;
public record Observation(String result, List<SessionView> sessions) {
    public Observation { sessions = List.copyOf(sessions); }
    public static Observation take(DeskAPI desk, Step step) {
        String result;
        try { result = "ok:"+step.run(desk); }
        catch (RuntimeException failure) {
            result = "error:"+failure.getClass().getName()+":"+String.valueOf(failure.getMessage());
        }
        return new Observation(result, desk.all());
    }
}
