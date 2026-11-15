package app;
import api.Ranges;
import java.util.*;
public final class RangeService implements Ranges {
    public RangeService() {}
    public List<Integer> values(int start,int end,int step) {return values(new RangeSpec(start,end,step));}
    public String describe(int start,int end,int step) {return describe(new RangeSpec(start,end,step));}
    public List<Integer> values(RangeSpec range) {
        Objects.requireNonNull(range,"range");
        List<Integer> result=new ArrayList<>();
        for(int value=range.start();value<=range.end();value+=range.step())result.add(value);
        return List.copyOf(result);
    }
    public String describe(RangeSpec range) {
        Objects.requireNonNull(range,"range");
        return range.start()+".."+range.end()+"/"+range.step()+":"+values(range);
    }
}
