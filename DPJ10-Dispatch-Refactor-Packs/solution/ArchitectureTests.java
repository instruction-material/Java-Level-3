import dispatch.app.Desk;
import dispatch.domain.Dispatch;
import dispatch.domain.Request;
import dispatch.domain.Request.Channel;
import dispatch.domain.Request.Priority;
import dispatch.infra.MemoryDispatches;
import dispatch.infra.StandardRenderer;
import dispatch.ports.DispatchRepository;
import dispatch.ports.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
public class ArchitectureTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++;if(!Objects.equals(actual,expected))throw new AssertionError("expected "+expected+" got "+actual);
    }
    private static void fails(Class<? extends Throwable> type,Runnable action) {
        checks++;
        try { action.run(); } catch(Throwable error) {
            if(type.isInstance(error))return;
            throw new AssertionError("expected "+type+" got "+error,error);
        }
        throw new AssertionError("expected "+type);
    }
    private static Request input(String id) { return new Request(id,"Hi",Channel.EMAIL,Priority.NORMAL,2); }
    public static void main(String[] args) {
        MemoryDispatches memory=new MemoryDispatches();List<String> trace=new ArrayList<>();int[] calls={0};
        Renderer renderer=request->{calls[0]++;trace.add("render:"+request.id());return new Dispatch(request.id(),"custom:"+request.text(),7);};
        DispatchRepository store=new DispatchRepository() {
            public Optional<Dispatch> find(String id){trace.add("find:"+id);return memory.find(id);}
            public List<Dispatch> all(){return memory.all();}
            public void insert(Dispatch dispatch){trace.add("insert:"+dispatch.id());memory.insert(dispatch);}
        };
        Desk desk=new Desk(store,renderer,message->trace.add("notify:"+message.id()));
        equal(desk.preview(input("a")),new Dispatch("a","custom:Hi",7));equal(calls[0],1);
        equal(memory.all(),List.of());equal(trace,List.of("render:a"));trace.clear();
        equal(desk.send(input("a")),new Dispatch("a","custom:Hi",7));equal(calls[0],2);
        equal(trace,List.of("find:a","render:a","insert:a","notify:a"));trace.clear();
        fails(IllegalArgumentException.class,()->desk.send(input("a")));equal(calls[0],2);
        equal(trace,List.of("find:a"));
        Renderer[] brokenRenderers=new Renderer[]{
            request->{throw new IllegalStateException("render");},
            request->null,
            request->new Dispatch("changed","text",0)};
        List<Class<? extends Throwable>> expectedFailures=List.of(IllegalStateException.class,NullPointerException.class,IllegalArgumentException.class);
        for(int index=0;index<brokenRenderers.length;index++) {
            Renderer broken=brokenRenderers[index];
            MemoryDispatches isolated=new MemoryDispatches();List<Dispatch> notifications=new ArrayList<>();
            Desk failing=new Desk(isolated,broken,notifications::add);
            fails(expectedFailures.get(index),()->failing.send(input("a")));
            equal(isolated.all(),List.of());equal(notifications,List.of());
        }
        List<Dispatch> notifications=new ArrayList<>();
        DispatchRepository unavailable=new DispatchRepository() {
            public Optional<Dispatch> find(String id){return Optional.empty();}
            public List<Dispatch> all(){return List.of();}
            public void insert(Dispatch dispatch){throw new IllegalStateException("store unavailable");}
        };
        Desk failedSave=new Desk(unavailable,new StandardRenderer(),notifications::add);
        fails(IllegalStateException.class,()->failedSave.send(input("a")));equal(notifications,List.of());
        MemoryDispatches afterCommit=new MemoryDispatches();
        Desk failedNotice=new Desk(afterCommit,new StandardRenderer(),message->{throw new IllegalStateException("notice");});
        fails(IllegalStateException.class,()->failedNotice.send(input("a")));
        equal(afterCommit.find("a").isPresent(),true);
        fails(NullPointerException.class,()->new Desk(null,renderer,message->{}));
        fails(NullPointerException.class,()->new Desk(memory,null,message->{}));
        fails(NullPointerException.class,()->new Desk(memory,renderer,null));
        MemoryDispatches ordered=new MemoryDispatches();
        ordered.insert(new Dispatch("b","one",1));ordered.insert(new Dispatch("a","two",2));
        List<Dispatch> old=ordered.all();ordered.insert(new Dispatch("c","three",3));
        equal(old.stream().map(Dispatch::id).toList(),List.of("b","a"));
        fails(UnsupportedOperationException.class,()->old.clear());
        fails(IllegalArgumentException.class,()->ordered.insert(new Dispatch("a","changed",3)));
        equal(ordered.find("a").orElseThrow(),new Dispatch("a","two",2));
        System.out.println("Architecture capstone contracts: "+checks+" passed");
    }
}
