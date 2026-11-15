import api.*;
import app.*;
import legacy.LegacyWorkshopDesk;
import preview.RenamePreview;
import trace.*;
import java.util.*;
import java.util.function.Supplier;

public final class ContractChecks {
    private static int checks;
    private ContractChecks() {}
    private static void ok(boolean value, String name) {
        checks++;
        if (!value) throw new AssertionError(name);
    }
    private static void eq(Object expected, Object actual, String name) {
        ok(Objects.equals(expected, actual), name+" expected="+expected+" actual="+actual);
    }
    private static void error(Class<? extends RuntimeException> type, String message, Runnable action) {
        checks++;
        try { action.run(); throw new AssertionError("missing "+type.getSimpleName()+":"+message); }
        catch (RuntimeException failure) {
            if (failure.getClass() != type || !Objects.equals(message, failure.getMessage()))
                throw new AssertionError("wrong error", failure);
        }
    }
    private static void immutable(List<?> values) {
        checks++;
        try { values.clear(); throw new AssertionError("mutable list"); }
        catch (UnsupportedOperationException expected) { /* expected */ }
    }
    private static void example(Supplier<DeskAPI> make) {
        DeskAPI desk=make.get();
        eq("Workshops 0\n",desk.report("plain"),"empty plain");
        eq("0\n",desk.report("compact"),"empty compact");
        desk.open("room",2); desk.open("quiet",1);
        eq(JoinStatus.ENROLLED,desk.join("room","ana"),"first seat");
        eq(JoinStatus.ENROLLED,desk.join("room","bo"),"second seat");
        eq(JoinStatus.WAITLISTED,desk.join("room","cy"),"queue");
        eq(JoinStatus.DUPLICATE,desk.join("room","cy"),"queued duplicate");
        SessionView retained=desk.view("room"); List<SessionView> previous=desk.all();
        eq(true,desk.leave("room","ana"),"leave seated");
        eq(List.of("bo","cy"),desk.view("room").enrolled(),"promotion order");
        eq(List.of(),desk.view("room").waiting(),"queue consumed");
        eq(List.of("ana","bo"),retained.enrolled(),"old snapshot unchanged");
        eq(retained,previous.get(0),"old catalog unchanged");
        eq(List.of("open","join:ana:enrolled","join:bo:enrolled","join:cy:waiting",
                   "leave:ana:enrolled","promote:cy"),desk.view("room").history(),"complete history");
        eq(true,desk.close("quiet"),"close");
        eq(false,desk.close("quiet"),"close repeat");
        String plain="Workshops 2\nroom capacity=2 open=true\nenrolled=bo,cy\nwaiting=-\n"
            +"quiet capacity=1 open=false\nenrolled=-\nwaiting=-\n";
        String compact="2\nroom|2|open|bo,cy|-\nquiet|1|closed|-|-\n";
        eq(plain,desk.report("plain"),"full plain"); eq(compact,desk.report("compact"),"full compact");
        List<SessionView> before=desk.all();
        desk.report("plain"); desk.report("compact"); desk.view("room"); desk.all();
        eq(before,desk.all(),"queries preserve state/history");
        immutable(previous); immutable(retained.enrolled()); immutable(retained.waiting()); immutable(retained.history());
        DeskAPI other=make.get(); eq(List.of(),other.all(),"independent storage");
    }
    private static void validation(Supplier<DeskAPI> make) {
        DeskAPI desk=make.get();
        error(NullPointerException.class,"session",()->desk.open(null,0));
        error(IllegalArgumentException.class,"session",()->desk.open("BAD",0));
        for(int capacity:new int[]{Integer.MIN_VALUE,-1,0,5,Integer.MAX_VALUE})
            error(IllegalArgumentException.class,"capacity",()->desk.open("room",capacity));
        eq(List.of(),desk.all(),"failed openings preserve empty catalog");
        desk.open("room",1); desk.join("room","ana"); desk.join("room","cy"); desk.join("room","bo");
        List<SessionView> before=desk.all();
        error(IllegalStateException.class,"session",()->desk.open("room",1));
        error(IllegalArgumentException.class,"capacity",()->desk.open("room",0));
        error(NullPointerException.class,"session",()->desk.join(null,null));
        error(NullPointerException.class,"participant",()->desk.join("missing",null));
        error(IllegalArgumentException.class,"participant",()->desk.leave("missing","BAD"));
        error(NoSuchElementException.class,"session",()->desk.join("missing","ana"));
        error(NoSuchElementException.class,"session",()->desk.leave("missing","ana"));
        error(NoSuchElementException.class,"session",()->desk.view("missing"));
        error(NoSuchElementException.class,"session",()->desk.close("missing"));
        error(NullPointerException.class,"style",()->desk.report(null));
        error(IllegalArgumentException.class,"style",()->desk.report("PLAIN"));
        error(IllegalArgumentException.class,"style",()->desk.report(""));
        eq(before,desk.all(),"invalid operations preserve complete state");
        eq(false,desk.leave("room","missing"),"absent leave");
        eq(JoinStatus.DUPLICATE,desk.join("room","ana"),"active duplicate");
        eq(before,desk.all(),"no-op operations preserve history");
        desk.leave("room","ana");
        eq(List.of("cy"),desk.view("room").enrolled(),"FIFO not lexical order");
        eq(List.of("bo"),desk.view("room").waiting(),"remaining FIFO");
        desk.close("room"); before=desk.all();
        error(NullPointerException.class,"participant",()->desk.join("room",null));
        error(IllegalStateException.class,"closed",()->desk.join("room","cy"));
        error(IllegalStateException.class,"closed",()->desk.leave("room","cy"));
        eq(before,desk.all(),"closed failures preserve state");
        for(String id:Arrays.asList("", " ", "BAD", "a|b", "x\n", "abcdefghijklmnopq")) {
            error(IllegalArgumentException.class,"session",()->desk.open(id,1));
            error(IllegalArgumentException.class,"participant",()->desk.join("missing",id));
        }
        DeskAPI edge=make.get();
        edge.open("abcdefghijklmnop",4); edge.join("abcdefghijklmnop","a-01234567890123");
        eq(1,edge.all().get(0).enrolled().size(),"long valid identifiers");
        List<String> people=new ArrayList<>(List.of("ana"));
        List<String> wait=new ArrayList<>(List.of("bo"));
        List<String> history=new ArrayList<>(List.of("open"));
        SessionView copy=new SessionView("room",1,true,people,wait,history);
        people.clear();wait.clear();history.clear();
        eq(List.of("ana"),copy.enrolled(),"constructor copies active");
        eq(List.of("bo"),copy.waiting(),"constructor copies waiting");
        eq(List.of("open"),copy.history(),"constructor copies history");
    }
    /** Independent array-list model keeps the oracle separate from production map layout. */
    private static final class Model implements DeskAPI {
        private static final class Session {
            final String id; final int capacity; boolean open=true;
            final List<String> active=new ArrayList<>(),queued=new ArrayList<>(),history=new ArrayList<>(List.of("open"));
            Session(String id,int capacity){this.id=id;this.capacity=capacity;}
            SessionView value(){return new SessionView(id,capacity,open,active,queued,history);}
        }
        private final List<Session> sessions=new ArrayList<>();
        private void id(String value,String name){
            if(value==null) throw new NullPointerException(name);
            if(!value.matches("[a-z][a-z0-9-]{0,15}")) throw new IllegalArgumentException(name);
        }
        private Session session(String name){
            return sessions.stream().filter(s->s.id.equals(name)).findFirst()
                .orElseThrow(()->new NoSuchElementException("session"));
        }
        public void open(String name,int capacity){
            id(name,"session");
            if(capacity<1||capacity>4) throw new IllegalArgumentException("capacity");
            if(sessions.stream().anyMatch(s->s.id.equals(name))) throw new IllegalStateException("session");
            sessions.add(new Session(name,capacity));
        }
        public JoinStatus join(String name,String person){
            id(name,"session");id(person,"participant");Session s=session(name);
            if(!s.open) throw new IllegalStateException("closed");
            if(s.active.contains(person)||s.queued.contains(person)) return JoinStatus.DUPLICATE;
            boolean seated=s.active.size()<s.capacity;
            (seated?s.active:s.queued).add(person);
            s.history.add("join:"+person+(seated?":enrolled":":waiting"));
            return seated?JoinStatus.ENROLLED:JoinStatus.WAITLISTED;
        }
        public boolean leave(String name,String person){
            id(name,"session");id(person,"participant");Session s=session(name);
            if(!s.open) throw new IllegalStateException("closed");
            int queued=s.queued.indexOf(person);
            if(queued>=0){s.queued.remove(queued);s.history.add("leave:"+person+":waiting");return true;}
            int active=s.active.indexOf(person);
            if(active<0)return false;
            s.active.remove(active);s.history.add("leave:"+person+":enrolled");
            if(!s.queued.isEmpty()){
                String promoted=s.queued.get(0);s.queued.remove(0);s.active.add(promoted);
                s.history.add("promote:"+promoted);
            }
            return true;
        }
        public boolean close(String name){
            id(name,"session");Session s=session(name);
            if(!s.open)return false;s.open=false;s.history.add("close");return true;
        }
        public SessionView view(String name){id(name,"session");return session(name).value();}
        public List<SessionView> all(){return sessions.stream().map(Session::value).toList();}
        public String report(String style){
            if(style==null)throw new NullPointerException("style");
            if(!style.equals("plain")&&!style.equals("compact"))throw new IllegalArgumentException("style");
            List<String> rows=new ArrayList<>();
            rows.add(style.equals("plain")?"Workshops "+sessions.size():Integer.toString(sessions.size()));
            for(Session s:sessions){
                String active=s.active.isEmpty()?"-":String.join(",",s.active);
                String queue=s.queued.isEmpty()?"-":String.join(",",s.queued);
                if(style.equals("plain")){
                    rows.add(s.id+" capacity="+s.capacity+" open="+s.open);
                    rows.add("enrolled="+active);rows.add("waiting="+queue);
                }else rows.add(s.id+"|"+s.capacity+"|"+(s.open?"open":"closed")+"|"+active+"|"+queue);
            }
            return String.join("\n",rows)+"\n";
        }
    }
    private static void sequences(Supplier<DeskAPI> make) {
        Random random=new Random(603015);
        List<String> people=Arrays.asList("ana","bo","cy","dee",null,"BAD");
        List<String> rooms=Arrays.asList("room","quiet","missing",null);
        for(int trial=0;trial<45;trial++){
            DeskAPI expected=new Model(),actual=make.get();
            List<Step> plan=new ArrayList<>();
            plan.add(new Step.Open("room",1+trial%4));plan.add(new Step.Open("quiet",2));
            plan.add(new Step.Join("room","cy"));plan.add(new Step.Join("room","ana"));
            for(int n=0;n<18;n++){
                String room=rooms.get(random.nextInt(rooms.size()));
                String person=people.get(random.nextInt(people.size()));
                plan.add(switch(random.nextInt(5)){
                    case 0->new Step.Join(room,person);
                    case 1->new Step.Leave(room,person);
                    case 2->new Step.Close(room);
                    case 3->new Step.Report(random.nextBoolean()?"plain":"compact");
                    default->new Step.Open(room,random.nextInt(7)-1);
                });
            }
            for(Step step:plan) {
                Observation before=Observation.take(expected,step),after=Observation.take(actual,step);
                eq(before,after,"model trace "+trial+" "+step);
                List<SessionView> retained=actual.all();
                eq(expected.report("plain"),actual.report("plain"),"model plain");
                eq(expected.report("compact"),actual.report("compact"),"model compact");
                eq(retained,actual.all(),"report query purity");
                for(SessionView view:retained){
                    ok(view.enrolled().size()<=view.capacity(),"capacity invariant");
                    Set<String> unique=new HashSet<>(view.enrolled());
                    unique.addAll(view.waiting());
                    eq(view.enrolled().size()+view.waiting().size(),unique.size(),"unique participation");
                }
            }
        }
    }
    private static void behavior(Supplier<DeskAPI> make) {
        example(make);validation(make);sequences(make);
        String input="open room 1\njoin room ana\njoin room bo\nleave room ana\nreport compact\n"
            +"close room\njoin room cy\nclose room\nunknown\nopen x nope\nreport plain\n";
        String wanted="opened\nENROLLED\nWAITLISTED\ntrue\n1\nroom|1|open|bo|-\ntrue\n"
            +"error:IllegalStateException:closed\nfalse\nerror:IllegalArgumentException:command\n"
            +"error:IllegalArgumentException:capacity\nWorkshops 1\nroom capacity=1 open=false\nenrolled=bo\nwaiting=-\n";
        eq(wanted,AppScript.run(make.get(),input.lines().toList()),"complete CLI script");
        DeskAPI boundary=make.get();
        error(NullPointerException.class,"desk",()->AppScript.run(null,null));
        error(NullPointerException.class,"lines",()->AppScript.run(boundary,null));
        List<String> invalid=new ArrayList<>();invalid.add("open room 1");invalid.add(null);
        checks++;
        try{AppScript.run(boundary,invalid);throw new AssertionError("null line");}
        catch(NullPointerException expected){ /* complete input validation before effects */ }
        eq(List.of(),boundary.all(),"null line prevents earlier effects");
        eq("opened\n",AppScript.run(boundary,List.of("  "," open room 1  ")),"trim and blank lines");
        String errors="error:IllegalArgumentException:command\nerror:IllegalArgumentException:command\n"
            +"error:IllegalArgumentException:capacity\nerror:IllegalArgumentException:capacity\n"
            +"room|1|true|[]|[]|[open]\n";
        eq(errors,AppScript.run(boundary,List.of("join room","unknown","open BAD nope","open room 0","view room")),"parse before domain validation");

    }
    private static class Wrapper implements DeskAPI {
        protected final DeskAPI delegate=new LegacyWorkshopDesk();
        protected int operations;
        public void open(String session,int capacity){delegate.open(session,capacity);operations++;}
        public JoinStatus join(String session,String person){JoinStatus s=delegate.join(session,person);operations++;return s;}
        public boolean leave(String session,String person){boolean b=delegate.leave(session,person);operations++;return b;}
        public boolean close(String session){boolean b=delegate.close(session);operations++;return b;}
        public SessionView view(String session){return delegate.view(session);}
        public List<SessionView> all(){return delegate.all();}
        public String report(String style){return delegate.report(style);}
    }
    private static List<Step> trace() {
        return List.of(new Step.Open("room",1),new Step.Join("room","ana"),new Step.Join("room","cy"),
            new Step.Join("room","bo"),new Step.Leave("room","ana"),new Step.Report("compact"),
            new Step.Join("missing",null),new Step.Close("room"),new Step.Join("room","dee"));
    }
    private static void transfer() {
        ok(Equivalence.same(new LegacyWorkshopDesk(),new WorkshopDesk(),trace()),"complete matching trace");
        DeskAPI wrongStatus=new Wrapper(){
            @Override public JoinStatus join(String s,String p){super.join(s,p);return JoinStatus.DUPLICATE;}
        };
        ok(!Equivalence.same(new LegacyWorkshopDesk(),wrongStatus,trace()),"return result matters");
        DeskAPI wrongMessage=new Wrapper(){
            @Override public JoinStatus join(String s,String p){
                if(p==null)throw new NullPointerException("wrong");
                return super.join(s,p);
            }
        };
        ok(!Equivalence.same(new LegacyWorkshopDesk(),wrongMessage,trace()),"exception message matters");
        DeskAPI wrongClass=new Wrapper(){
            @Override public JoinStatus join(String s,String p){
                if(p==null)throw new IllegalArgumentException("participant");
                return super.join(s,p);
            }
        };
        ok(!Equivalence.same(new LegacyWorkshopDesk(),wrongClass,trace()),"exception class matters");
        DeskAPI transientView=new Wrapper(){
            @Override public List<SessionView> all(){return operations==1?List.of():super.all();}
        };
        List<Step> recovered=List.of(new Step.Open("room",1),new Step.Join("room","ana"),new Step.Leave("room","ana"));
        ok(!Equivalence.same(new LegacyWorkshopDesk(),transientView,recovered),"intermediate snapshot matters");
        Wrapper one=new Wrapper(),two=new Wrapper();
        ok(Equivalence.same(one,two,List.of()),"empty plan");
        eq(0,one.operations+two.operations,"empty plan has no calls");
        error(NullPointerException.class,"before",()->Equivalence.same(null,two,List.of()));
        error(NullPointerException.class,"after",()->Equivalence.same(one,null,List.of()));
        error(NullPointerException.class,"steps",()->Equivalence.same(one,two,null));
        error(IllegalArgumentException.class,"independent desks",()->Equivalence.same(one,one,List.of()));
        List<Step> supplied=new ArrayList<>(trace());
        ok(Equivalence.same(new LegacyWorkshopDesk(),new WorkshopDesk(),supplied),"mutable input supported");
        eq(trace(),supplied,"script retained");
        List<Step> bad=new ArrayList<>();bad.add(new Step.Open("room",1));bad.add(null);
        checks++;
        try{Equivalence.same(one,two,bad);throw new AssertionError("null step");}
        catch(NullPointerException expected){ /* validate complete plan before effects */ }
        eq(0,one.operations+two.operations,"invalid plan has no calls");
    }
    private static void extension() {
        DeskAPI desk=new LegacyWorkshopDesk();desk.open("room",1);
        desk.join("room","ana");desk.join("room","cy");desk.join("room","bo");
        SessionView before=desk.view("room");
        SessionView after=RenamePreview.rename(before,"cy","dee");
        eq(List.of("ana"),after.enrolled(),"rename active retained");
        eq(List.of("dee","bo"),after.waiting(),"rename retains queue position");
        eq(List.of("cy","bo"),before.waiting(),"input queue retained");
        eq(before,desk.view("room"),"preview leaves live desk unchanged");
        List<String> expected=new ArrayList<>(before.history());expected.add("rename:cy:dee");
        eq(expected,after.history(),"history preserved and one event appended");
        eq(before.id(),after.id(),"session retained");eq(before.capacity(),after.capacity(),"capacity retained");
        eq(before.open(),after.open(),"open flag retained");
        SessionView active=RenamePreview.rename(before,"ana","amy");
        eq(List.of("amy"),active.enrolled(),"active rename");eq(before.waiting(),active.waiting(),"other list retained");
        ok(before==RenamePreview.rename(before,"missing","cy"),"missing old is same snapshot");
        ok(before==RenamePreview.rename(before,"ana","ana"),"identity rename is same snapshot");
        error(NullPointerException.class,"snapshot",()->RenamePreview.rename(null,null,null));
        error(NullPointerException.class,"oldID",()->RenamePreview.rename(before,null,null));
        error(NullPointerException.class,"newID",()->RenamePreview.rename(before,"ana",null));
        error(IllegalArgumentException.class,"oldID",()->RenamePreview.rename(before,"BAD","BAD"));
        error(IllegalArgumentException.class,"newID",()->RenamePreview.rename(before,"ana","BAD"));
        error(IllegalArgumentException.class,"newID",()->RenamePreview.rename(before,"ana","cy"));
        error(IllegalArgumentException.class,"newID",()->RenamePreview.rename(before,"cy","bo"));
        immutable(after.enrolled());immutable(after.waiting());immutable(after.history());
        desk.close("room");SessionView closed=desk.view("room");
        error(IllegalStateException.class,"closed",()->RenamePreview.rename(closed,"missing","dee"));
        eq(before.enrolled(),desk.view("room").enrolled(),"all previews leave existing roster");
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode){
            case "baseline"->{behavior(LegacyWorkshopDesk::new);behavior(WorkshopDesk::new);}
            case "core"->behavior(WorkshopDesk::new);
            case "transfer"->transfer();
            case "extension"->extension();
            case "all"->{behavior(WorkshopDesk::new);transfer();extension();}
            default->throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+checks+" checks passed");
    }
}
