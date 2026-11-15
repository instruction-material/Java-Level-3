import api.*;
import app.BoardReport;
import legacy.LegacyBoard;
import transfer.SnapshotDiff;
import extension.BoardPreview;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void behavior(Supplier<BoardAPI> make) {
        BoardAPI board=make.get();
        same("tasks=3\ndone=1\nremaining=15\nopen=read,draw\n",board.summary(Main.sample()),"hand report");
        same(15,board.remainingMinutes(Main.sample()),"only unfinished minutes");
        same("tasks=0\ndone=0\nremaining=0\nopen=-\n",board.summary(List.of()),"empty report");
        Random random=new Random(617015);
        for(int n=0;n<=8;n++)for(int trial=0;trial<20;trial++) {
            List<Task> input=new ArrayList<>();
            for(int k=0;k<n;k++)input.add(new Task("t"+k,random.nextInt(121),random.nextBoolean()));
            List<Task> retained=List.copyOf(input);
            int completed=(int)input.stream().filter(Task::done).count();
            int remaining=input.stream().filter(t->!t.done()).mapToInt(Task::minutes).sum();
            List<String> titles=input.stream().filter(t->!t.done()).map(Task::title).toList();
            String expected="tasks="+n+"\ndone="+completed+"\nremaining="+remaining+"\nopen="
                +(titles.isEmpty()?"-":String.join(",",titles))+"\n";
            same(expected,board.summary(input),"independent model");
            same(remaining,board.remainingMinutes(input),"model remaining");
            same(retained,input,"read purity");
        }
        List<Task> duplicate=List.of(new Task("same",0,false),new Task("same",120,false));
        same("tasks=2\ndone=0\nremaining=120\nopen=same,same\n",board.summary(duplicate),"duplicate titles retained");
        same(960,board.remainingMinutes(Collections.nCopies(8,new Task("x",120,false))),"maximum");
        fails(NullPointerException.class,"tasks",()->board.summary(null));
        fails(IllegalArgumentException.class,"tasks",()->board.remainingMinutes(Collections.nCopies(9,new Task("x",0,false))));
        List<Task> bad=new ArrayList<>();bad.add(null);
        fails(NullPointerException.class,"task",()->board.summary(bad));
        fails(NullPointerException.class,"title",()->new Task(null,-1,false));
        fails(IllegalArgumentException.class,"title",()->new Task("bad,",-1,false));
        fails(IllegalArgumentException.class,"minutes",()->new Task("ok",121,false));
        List<String> supplied=new ArrayList<>(List.of("a"));
        BoardStats stats=new BoardStats(1,0,3,supplied);supplied.clear();
        same(List.of("a"),stats.openTitles(),"stats copy");immutable(stats.openTitles());
    }
    private static void transfer() {
        same(1,SnapshotDiff.firstDifference(List.of("before","step1","final"),List.of("before","bad","final")),"intermediate mismatch");
        same(-1,SnapshotDiff.firstDifference(List.of("a","b"),List.of("a","b")),"equal");
        same(1,SnapshotDiff.firstDifference(List.of("a"),List.of("a","b")),"length prefix");
        same(0,SnapshotDiff.firstDifference(List.of(),List.of("a")),"empty prefix");
        same(-1,SnapshotDiff.firstDifference(List.of(),List.of()),"empty equal");
        same(0,SnapshotDiff.firstDifference(List.of("a\n"),List.of("a")),"exact newlines");
        fails(NullPointerException.class,"before",()->SnapshotDiff.firstDifference(null,null));
        fails(NullPointerException.class,"after",()->SnapshotDiff.firstDifference(List.of(),null));
        List<String> bad=new ArrayList<>();bad.add(null);
        try{SnapshotDiff.firstDifference(bad,List.of());throw new AssertionError("null capture");}
        catch(NullPointerException expected){yes(true,"null capture rejected");}
        List<String> input=new ArrayList<>(List.of("a","b"));
        same(0,SnapshotDiff.firstDifference(input,List.of("x","b")),"first index");same(List.of("a","b"),input,"captures retained");
    }
    private static void extension() {
        List<Task> input=new ArrayList<>(Main.sample());
        List<Task> sorted=BoardPreview.sorted(input);
        same(List.of("code","draw","read"),sorted.stream().map(Task::title).toList(),"new sorted view");
        same(Main.sample(),input,"original order retained");immutable(sorted);
        Task first=new Task("a",1,false),second=new Task("a",2,true);
        same(List.of(first,second),BoardPreview.sorted(List.of(first,second)),"stable duplicate titles");
        same(List.of(),BoardPreview.sorted(List.of()),"empty view");
        fails(NullPointerException.class,"tasks",()->BoardPreview.sorted(null));
        List<Task> bad=new ArrayList<>();bad.add(null);
        fails(NullPointerException.class,"task",()->BoardPreview.sorted(bad));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyBoard::new);behavior(BoardReport::new);}
            case "core" -> behavior(BoardReport::new);
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {behavior(BoardReport::new);transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
