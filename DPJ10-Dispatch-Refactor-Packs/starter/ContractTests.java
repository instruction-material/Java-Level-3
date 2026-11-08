import dispatch.Composition;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.domain.Request.Channel;
import dispatch.domain.Request.Priority;
import dispatch.legacy.LegacyDesk;
import dispatch.ports.DeskApi;
import dispatch.ports.NoticeSink;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++;
        if(!Objects.equals(actual,expected))throw new AssertionError("expected "+expected+" got "+actual);
    }
    private static void fails(Class<? extends Throwable> type,Runnable action) {
        checks++;
        try { action.run(); } catch(Throwable error) {
            if(type.isInstance(error))return;
            throw new AssertionError("expected "+type+" got "+error,error);
        }
        throw new AssertionError("expected "+type);
    }
    private static Request request(String id) { return new Request(id,"Hi",Channel.EMAIL,Priority.NORMAL,2); }
    private static Dispatch oracle(Request value) {
        int[][] table={{100,140},{60,100}};
        String[][] prefixes={{"EMAIL: ","URGENT EMAIL: "},{"SMS: ","URGENT SMS: "}};
        return new Dispatch(value.id(),prefixes[value.channel().ordinal()][value.priority().ordinal()]+value.text(),
            table[value.channel().ordinal()][value.priority().ordinal()]*value.copies());
    }
    private static void behavior(Function<NoticeSink,DeskApi> factory) {
        for(int copies=1;copies<=20;copies++)for(Channel channel:Channel.values())for(Priority priority:Priority.values())for(String text:new String[]{"","Hi","a, \"b\""}) {
            List<Dispatch> notices=new ArrayList<>();
            DeskApi desk=factory.apply(notices::add);
            Request input=new Request("case-"+copies,text,channel,priority,copies);
            Dispatch expected=oracle(input);
            equal(desk.preview(input),expected);equal(desk.all(),List.of());equal(notices,List.of());
            equal(desk.send(input),expected);equal(desk.find(input.id()).orElseThrow(),expected);
            equal(desk.all(),List.of(expected));equal(notices,List.of(expected));
            fails(IllegalArgumentException.class,()->desk.send(input));
            equal(desk.all(),List.of(expected));equal(notices,List.of(expected));
        }
        List<Dispatch> notices=new ArrayList<>();
        DeskApi desk=factory.apply(notices::add);
        equal(desk.find("missing").isEmpty(),true);
        fails(NullPointerException.class,()->desk.preview(null));
        fails(NullPointerException.class,()->desk.send(null));
        fails(NullPointerException.class,()->desk.find(null));
        fails(IllegalArgumentException.class,()->desk.find("Bad"));
        desk.send(request("b"));desk.send(request("a"));
        List<Dispatch> old=desk.all();
        fails(UnsupportedOperationException.class,()->old.clear());
        desk.send(request("c"));
        equal(old.stream().map(Dispatch::id).toList(),List.of("b","a"));
        equal(desk.all().stream().map(Dispatch::id).toList(),List.of("b","a","c"));
        DeskApi[] holder={null};
        List<String> observations=new ArrayList<>();
        holder[0]=factory.apply(value->{
            equal(holder[0].find(value.id()),Optional.of(value));
            equal(holder[0].preview(request("read")).id(),"read");
            fails(IllegalStateException.class,()->holder[0].send(request("nested")));
            observations.add(value.id());
        });
        holder[0].send(request("first"));holder[0].send(request("next"));
        equal(observations,List.of("first","next"));equal(holder[0].find("nested").isEmpty(),true);
        DeskApi failure=factory.apply(value->{if(value.id().equals("bad"))throw new IllegalStateException("notification");});
        fails(IllegalStateException.class,()->failure.send(request("bad")));
        equal(failure.find("bad").orElseThrow(),oracle(request("bad")));
        fails(IllegalArgumentException.class,()->failure.send(request("bad")));
        equal(failure.send(request("good")),oracle(request("good")));
        fails(NullPointerException.class,()->factory.apply(null));
    }
    private static void boundaries() {
        fails(NullPointerException.class,()->new Request(null,"",Channel.EMAIL,Priority.NORMAL,1));
        fails(IllegalArgumentException.class,()->new Request("Bad","",Channel.EMAIL,Priority.NORMAL,1));
        fails(NullPointerException.class,()->new Request("a",null,Channel.EMAIL,Priority.NORMAL,1));
        fails(IllegalArgumentException.class,()->new Request("a","a\nb",Channel.EMAIL,Priority.NORMAL,1));
        fails(IllegalArgumentException.class,()->new Request("a","a\rb",Channel.EMAIL,Priority.NORMAL,1));
        fails(NullPointerException.class,()->new Request("a","",null,Priority.NORMAL,1));
        fails(NullPointerException.class,()->new Request("a","",Channel.EMAIL,null,1));
        fails(IllegalArgumentException.class,()->new Request("a","",Channel.EMAIL,Priority.NORMAL,0));
        fails(IllegalArgumentException.class,()->new Request("a","",Channel.EMAIL,Priority.NORMAL,21));
        fails(IllegalArgumentException.class,()->new Dispatch("a","",-1));
        fails(NullPointerException.class,()->new Dispatch("a",null,0));
        fails(IllegalArgumentException.class,()->new Dispatch("a","a\nb",0));
    }
    private static void optional() {
        List<Dispatch> notices=new ArrayList<>();
        DeskApi desk=Composition.create(notices::add);
        equal(desk.sendBatch(List.of()),List.of());
        List<Dispatch> result=desk.sendBatch(List.of(request("a"),request("b")));
        equal(result,List.of(oracle(request("a")),oracle(request("b"))));
        fails(UnsupportedOperationException.class,()->result.clear());
        fails(NullPointerException.class,()->desk.sendBatch(null));
        fails(NullPointerException.class,()->desk.sendBatch(Arrays.asList(request("before-null"),null)));
        equal(desk.find("before-null").isEmpty(),true);
        fails(IllegalArgumentException.class,()->desk.sendBatch(List.of(request("c"),request("a"),request("d"))));
        equal(desk.all().stream().map(Dispatch::id).toList(),List.of("a","b","c"));
        equal(notices.stream().map(Dispatch::id).toList(),List.of("a","b","c"));
        DeskApi broken=Composition.create(value->{if(value.id().equals("b"))throw new IllegalStateException("notification");});
        fails(IllegalStateException.class,()->broken.sendBatch(List.of(request("a"),request("b"),request("c"))));
        equal(broken.all().stream().map(Dispatch::id).toList(),List.of("a","b"));
        DeskApi[] holder={null};List<Request> mutable=new ArrayList<>(List.of(request("a"),request("b")));
        holder[0]=Composition.create(value->{
            mutable.clear();
            fails(IllegalStateException.class,()->holder[0].sendBatch(List.of()));
        });
        equal(holder[0].sendBatch(mutable).stream().map(Dispatch::id).toList(),List.of("a","b"));
        equal(mutable,List.of());
    }
    public static void main(String[] args) {
        if(args.length>1 || (args.length==1 && !args[0].equals("base") && !args[0].equals("core")))throw new IllegalArgumentException("use base, core or no arguments");
        boundaries();behavior(LegacyDesk::new);
        boolean base=args.length==1 && args[0].equals("base");
        if(!base)behavior(Composition::create);
        String label=base?"Baseline":"Core";
        if(args.length==0){optional();label="Complete";}
        System.out.println(label+" capstone contracts: "+checks+" passed");
    }
}
