import api.*;
import app.*;
import legacy.LegacyRanges;
import transfer.EveryOther;
import extension.MergeRanges;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void nullMember(Runnable work) {
        try{work.run();throw new AssertionError("null member");}
        catch(NullPointerException expected){yes(true,"null member rejected");}
    }
    private static void behavior(Supplier<Ranges> make) {
        Ranges ranges=make.get();
        same(List.of(2,5,8),ranges.values(2,9,3),"nonlanding end");
        same("2..9/3:[2, 5, 8]",ranges.describe(2,9,3),"exact description");
        same(List.of(100),ranges.values(100,100,20),"equal endpoints");
        for(int start:new int[]{0,1,20,99,100})for(int end:new int[]{0,2,21,100})
            for(int step:new int[]{1,2,7,20}) {
                if(end<start)continue;
                List<Integer> expected=new ArrayList<>();
                for(int index=0;index<101;index++)if(index>=start&&index<=end&&(index-start)%step==0)expected.add(index);
                List<Integer> actual=ranges.values(start,end,step);
                same(expected,actual,"independent membership model");immutable(actual);
                same(start+".."+end+"/"+step+":"+expected,ranges.describe(start,end,step),"clump description");
            }
        fails(IllegalArgumentException.class,"start",()->ranges.values(-1,-1,0));
        fails(IllegalArgumentException.class,"end",()->ranges.values(0,101,0));
        fails(IllegalArgumentException.class,"step",()->ranges.values(10,0,0));
        fails(IllegalArgumentException.class,"range",()->ranges.values(10,0,1));
        fails(IllegalArgumentException.class,"step",()->ranges.values(0,100,21));
    }
    private static void core() {
        behavior(RangeService::new);
        RangeSpec range=new RangeSpec(2,9,3);
        RangeService service=new RangeService();
        same(2,range.start(),"start accessor");same(9,range.end(),"end accessor");same(3,range.step(),"step accessor");
        same(List.of(2,5,8),service.values(range),"whole object values");
        same("2..9/3:[2, 5, 8]",service.describe(range),"whole object description");
        same(service.values(2,9,3),service.values(range),"old adapter matches");
        fails(NullPointerException.class,"range",()->service.values(null));
        fails(NullPointerException.class,"range",()->service.describe(null));
        fails(IllegalArgumentException.class,"start",()->new RangeSpec(-1,-1,0));
        fails(IllegalArgumentException.class,"end",()->new RangeSpec(0,101,0));
        fails(IllegalArgumentException.class,"step",()->new RangeSpec(10,0,0));
        fails(IllegalArgumentException.class,"range",()->new RangeSpec(10,0,1));
    }
    private static void transfer() {
        List<Integer> input=new ArrayList<>(List.of(8,1,8,2,9));
        List<Integer> result=EveryOther.select(input);
        same(List.of(8,8,9),result,"indices not values");
        same(List.of(8,1,8,2,9),input,"input retained");immutable(result);
        same(List.of(),EveryOther.select(List.of()),"empty");
        same(List.of(-1),EveryOther.select(List.of(-1)),"single negative preserved");
        fails(NullPointerException.class,"values",()->EveryOther.select(null));
        List<Integer> bad=new ArrayList<>(List.of(1));bad.add(null);
        nullMember(()->EveryOther.select(bad));
        for(int size=0;size<10;size++) {
            List<Integer> values=new ArrayList<>(),expected=new ArrayList<>();
            for(int n=0;n<size;n++){values.add(n);if(n%2==0)expected.add(n);}
            same(expected,EveryOther.select(values),"index table");
        }
    }
    private static void extension() {
        List<Integer> one=new ArrayList<>(List.of(8,2,5,2)),two=new ArrayList<>(List.of(9,1,5));
        List<Integer> result=MergeRanges.unique(one,two);
        same(List.of(1,2,5,8,9),result,"sorted unique");immutable(result);
        same(List.of(8,2,5,2),one,"first retained");same(List.of(9,1,5),two,"second retained");
        same(List.of(),MergeRanges.unique(List.of(),List.of()),"empty merge");
        same(List.of(Integer.MIN_VALUE,0,Integer.MAX_VALUE),MergeRanges.unique(
            List.of(Integer.MAX_VALUE,0),List.of(Integer.MIN_VALUE,0)),"no subtraction overflow");
        fails(NullPointerException.class,"first",()->MergeRanges.unique(null,null));
        fails(NullPointerException.class,"second",()->MergeRanges.unique(List.of(),null));
        List<Integer> bad=new ArrayList<>();bad.add(null);
        nullMember(()->MergeRanges.unique(bad,List.of()));
        nullMember(()->MergeRanges.unique(List.of(),bad));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyRanges::new);behavior(RangeService::new);}
            case "core" -> core();
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {core();transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
