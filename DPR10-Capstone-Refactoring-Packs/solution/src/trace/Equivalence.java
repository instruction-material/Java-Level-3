package trace;
import api.DeskAPI;
import java.util.*;
public final class Equivalence {
    private Equivalence() {}
    public static boolean same(DeskAPI before, DeskAPI after, List<Step> steps) {
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(after, "after");
        List<Step> plan = List.copyOf(Objects.requireNonNull(steps, "steps"));
        if (before == after) throw new IllegalArgumentException("independent desks");
        for (Step step : plan)
            if (!Observation.take(before, step).equals(Observation.take(after, step))) return false;
        return true;
    }
}
