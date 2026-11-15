package app;
import api.Ranges;
import legacy.LegacyRanges;
import java.util.List;
public final class RangeService implements Ranges {
    public RangeService() {}
    public List<Integer> values(int start,int end,int step) {api.RangeRules.validate(start,end,step);
        java.util.List<Integer> result=new java.util.ArrayList<>();
        for(int value=start;value<=end;value+=step)result.add(value);
        return java.util.List.copyOf(result);}
    public String describe(int start,int end,int step) {return start+".."+end+"/"+step+":"+values(start,end,step);}
    public List<Integer> values(RangeSpec range) {throw new UnsupportedOperationException("Complete parameter object");}
    public String describe(RangeSpec range) {throw new UnsupportedOperationException("Complete parameter object");}
}
