import api.*;
import app.BoardReport;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        BoardAPI board=new BoardReport();
        if(board.remainingMinutes(Main.sample())!=15)throw new AssertionError("remaining");
        if(!board.summary(Main.sample()).endsWith("open=read,draw\n"))throw new AssertionError("encounter order");
        System.out.println("compatibility passed");
    }
}
