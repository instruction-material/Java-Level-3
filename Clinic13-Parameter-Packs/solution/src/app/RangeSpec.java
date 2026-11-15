package app;
import api.RangeRules;
public record RangeSpec(int start,int end,int step) {
    public RangeSpec {RangeRules.validate(start,end,step);}
}
