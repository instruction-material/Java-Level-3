package app;
import api.*;
import java.util.List;
public final class BoardReport implements BoardAPI {
    public BoardReport() {}
    public String summary(List<Task> tasks) {return BoardText.format(new BoardAnalysis(tasks).stats());}
    public int remainingMinutes(List<Task> tasks) {return new BoardAnalysis(tasks).stats().remaining();}
}
