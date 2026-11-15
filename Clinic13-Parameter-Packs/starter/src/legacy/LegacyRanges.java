package legacy;
import api.*;
import java.util.*;
public final class LegacyRanges implements Ranges {
    public LegacyRanges() {}
    public List<Integer> values(int start,int end,int step) {
        RangeRules.validate(start,end,step);
        List<Integer> result=new ArrayList<>();
        for(int value=start;value<=end;value+=step)result.add(value);
        return List.copyOf(result);
    }
    public String describe(int start,int end,int step) {
        return start+".."+end+"/"+step+":"+values(start,end,step);
    }
}
