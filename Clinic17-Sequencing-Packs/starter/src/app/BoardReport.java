package app;
import api.*;
import java.util.*;
public final class BoardReport implements BoardAPI {
    public BoardReport() {}
    public String summary(List<Task> tasks) {
        List<Task> input=BoardRows.copy(tasks);
        int done=0;
        for(Task task:input)if(task.done())done++;
        int remaining=0;
        for(Task task:input)if(!task.done())remaining+=task.minutes();
        List<String> titles=new ArrayList<>();
        for(Task task:input)if(!task.done())titles.add(task.title());
        return "tasks="+input.size()+"\ndone="+done+"\nremaining="+remaining+"\nopen="
            +(titles.isEmpty()?"-":String.join(",",titles))+"\n";
    }
    public int remainingMinutes(List<Task> tasks) {
        List<Task> input=BoardRows.copy(tasks);int remaining=0;
        for(Task task:input)if(!task.done())remaining+=task.minutes();
        return remaining;
    }
}
