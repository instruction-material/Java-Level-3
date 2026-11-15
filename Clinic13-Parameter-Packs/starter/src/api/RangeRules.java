package api;
public final class RangeRules {
    private RangeRules() {}
    public static void validate(int start,int end,int step) {
        if(start<0||start>100)throw new IllegalArgumentException("start");
        if(end<0||end>100)throw new IllegalArgumentException("end");
        if(step<1||step>20)throw new IllegalArgumentException("step");
        if(end<start)throw new IllegalArgumentException("range");
    }
}
