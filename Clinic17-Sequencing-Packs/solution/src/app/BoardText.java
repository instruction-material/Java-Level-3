package app;
import api.BoardStats;
final class BoardText {
    private BoardText() {}
    static String format(BoardStats stats) {
        return "tasks="+stats.tasks()+"\ndone="+stats.done()+"\nremaining="+stats.remaining()+"\nopen="
            +(stats.openTitles().isEmpty()?"-":String.join(",",stats.openTitles()))+"\n";
    }
}
