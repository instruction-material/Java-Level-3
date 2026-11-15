package app;
import api.*;
import java.util.*;
final class BoardAnalysis {
    private final List<Task> tasks;
    BoardAnalysis(List<Task> tasks) {this.tasks=BoardRows.copy(tasks);}
    BoardStats stats() {
        int done=0,remaining=0;List<String> titles=new ArrayList<>();
        for(Task task:tasks) {
            if(task.done())done++;
            else {remaining+=task.minutes();titles.add(task.title());}
        }
        return new BoardStats(tasks.size(),done,remaining,titles);
    }
}
