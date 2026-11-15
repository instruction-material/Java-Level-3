package transfer;
import api.*;
import java.util.*;
public final class WorkflowTrace {
    private WorkflowTrace() {}
    public static List<String> stages(List<String> rows,StepHook hook) {
        List<String> input=Rows.copy(rows);Objects.requireNonNull(hook,"hook");
        List<String> completed=new ArrayList<>();
        hook.accept("start","");completed.add("start");
        for(String row:input){hook.accept("row",row);completed.add("row:"+row);}
        hook.accept("end","");completed.add("end");
        return List.copyOf(completed);
    }
}
