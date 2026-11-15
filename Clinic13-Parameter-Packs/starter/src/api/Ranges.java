package api;
import java.util.List;
public interface Ranges {
    List<Integer> values(int start,int end,int step);
    String describe(int start,int end,int step);
}
