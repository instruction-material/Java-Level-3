import journal.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.function.*;
public final class ContractChecks {
    private static long checks;
    private ContractChecks() {}
    private static void eq(Object expected, Object actual) {
        checks++; if (!Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    private static void fails(Class<? extends Throwable> type, String message, Runnable code) {
        try { code.run(); } catch (Throwable error) { eq(type,error.getClass()); if (message!=null) eq(message,error.getMessage()); return; }
        throw new AssertionError("Expected " + type.getName());
    }
    private static JournalAPI injected(Clock clock, IntSupplier ids, EntrySink sink) {
        try { return DispatchJournal.class.getConstructor(Clock.class,IntSupplier.class,EntrySink.class).newInstance(clock,ids,sink); }
        catch (NoSuchMethodException error) { throw new AssertionError("Missing injection constructor",error); }
        catch (InvocationTargetException error) { if(error.getCause() instanceof RuntimeException runtime) throw runtime; throw new AssertionError(error); }
        catch (ReflectiveOperationException error) { throw new AssertionError(error); }
    }
    private static void baseline(JournalAPI journal) {
        PrintStream original=System.out;
        try(ByteArrayOutputStream bytes=new ByteArrayOutputStream(); PrintStream output=new PrintStream(bytes,true,StandardCharsets.UTF_8)) {
            System.setOut(output);
            for(String label:List.of("lamp"," A B ","é","17")) for(int count:List.of(0,1,2,99)) {
                bytes.reset(); Instant before=Instant.now().minusSeconds(5);
                Entry entry=journal.record(label,count); Instant after=Instant.now().plusSeconds(5);
                eq(label,entry.label());eq(count,entry.count());eq(true,entry.id()>=0&&entry.id()<1_000_000);
                eq(true,!entry.when().isBefore(before)&&!entry.when().isAfter(after));
                eq(entry.when()+"|"+entry.id()+"|"+label+"|"+count+"\n",bytes.toString(StandardCharsets.UTF_8));
            }
            bytes.reset();
            fails(NullPointerException.class,"label",()->journal.record(null,-1));
            for(String label:List.of(""," ","a|b","a\nb","a\rb")) fails(IllegalArgumentException.class,"label",()->journal.record(label,-1));
            for(int count:List.of(-1,100,Integer.MIN_VALUE,Integer.MAX_VALUE)) fails(IllegalArgumentException.class,"count",()->journal.record("ok",count));
            eq("",bytes.toString(StandardCharsets.UTF_8));
        } catch(IOException error) { throw new UncheckedIOException(error); }
        finally { System.setOut(original); }
    }
    private static final class RecordingClock extends Clock {
        private final List<String> events;
        RecordingClock(List<String> events) { this.events=events; }
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId zone) { Objects.requireNonNull(zone); return Clock.fixed(Instant.parse("2026-11-15T12:00:00Z"),zone); }
        public Instant instant() { events.add("clock"); return Instant.parse("2026-11-15T12:00:00Z"); }
    }
    private static void core() {
        List<String> events=Collections.synchronizedList(new ArrayList<>()); List<Entry> written=new ArrayList<>();
        Clock clock=new RecordingClock(events);
        IntSupplier ids=()->{events.add("id");return 17;};
        EntrySink sink=entry->{events.add("sink");written.add(entry);};
        JournalAPI journal=injected(clock,ids,sink);
        for(String label:List.of("lamp"," A B ","é","17")) for(int count=0;count<=99;count++) {
            events.clear();written.clear();
            Entry entry=journal.record(label,count);
            eq(List.of("clock","id","sink"),events);eq(List.of(entry),written);
            eq(Instant.parse("2026-11-15T12:00:00Z"),entry.when());eq(17,entry.id());eq(label,entry.label());eq(count,entry.count());
            eq("2026-11-15T12:00:00Z|17|"+label+"|"+count+"\n",entry.line());
        }
        events.clear();written.clear();
        fails(NullPointerException.class,"label",()->journal.record(null,-1));
        for(String label:List.of(""," ","a|b","a\nb","a\rb")) fails(IllegalArgumentException.class,"label",()->journal.record(label,-1));
        fails(IllegalArgumentException.class,"count",()->journal.record("x",100));
        eq(List.of(),events);eq(List.of(),written);
        fails(NullPointerException.class,"clock",()->injected(null,null,null));
        fails(NullPointerException.class,"ids",()->injected(clock,null,null));
        fails(NullPointerException.class,"sink",()->injected(clock,ids,null));
        for(int id:List.of(0,999999)) eq(id,injected(clock,()->id,sink).record("x",0).id());
        for(int id:List.of(-1,1000000)) {
            events.clear();written.clear();
            fails(IllegalArgumentException.class,"id",()->injected(clock,()->id,sink).record("x",0));eq(List.of(),written);
        }
        IOException failure=new IOException("offline");events.clear();
        JournalAPI failing=injected(clock,ids,entry->{events.add("sink");throw failure;});
        try { failing.record("x",1);throw new AssertionError("Sink failure swallowed"); }
        catch(UncheckedIOException error) { eq(failure,error.getCause());eq("journal",error.getMessage()); }
        eq(List.of("clock","id","sink"),events);
        eq("2026-11-15T12:00:00Z|17|lamp|2\n",CompatibilityClient.record(injected(Clock.fixed(Instant.parse("2026-11-15T12:00:00Z"),ZoneOffset.UTC),()->17,entry->{})).line());
    }
    private static void transfer() {
        for(Instant time:List.of(Instant.EPOCH,Instant.parse("2000-02-29T12:00:00Z"),Instant.parse("2026-11-15T12:00:00Z")))
            for(int id:List.of(0,17,999999)) for(String label:List.of("lamp"," A B ","é","17")) for(int count:List.of(0,1,2,99))
                eq("<time>|<id>|"+label+"|"+count+"\n",CaptureNormalizer.normalize(new Entry(time,id,label,count).line()));
        fails(NullPointerException.class,"line",()->CaptureNormalizer.normalize(null));
        for(String bad:List.of("","2026-11-15T12:00:00Z|17|lamp|2","bad|17|lamp|2\n","2026-11-15T12:00:00Z|-1|lamp|2\n","2026-11-15T12:00:00Z|17|lamp|100\n","2026-11-15T12:00:00Z|17||2\n","2026-11-15T12:00:00Z|17|lamp|2\n\n","2026-11-15T12:00:00Z|17|lamp|02\n"))
            fails(IllegalArgumentException.class,"line",()->CaptureNormalizer.normalize(bad));
        eq(false,CaptureNormalizer.normalize("2026-11-15T12:00:00Z|17|lamp|1\n").equals(CaptureNormalizer.normalize("2026-11-15T12:00:00Z|17|lamp|2\n")));
    }
    private static void extension() throws IOException {
        for(int id=0;id<100;id++) {
            Entry entry=new Entry(Instant.EPOCH,id,"lamp",2);List<Entry> calls=new ArrayList<>();
            IOException first=new IOException("first"),second=new IOException("second");
            RetryingSink success=new RetryingSink(value->calls.add(value));success.write(entry);eq(List.of(entry),calls);
            calls.clear();
            new RetryingSink(value->{calls.add(value);if(calls.size()==1)throw first;}).write(entry);
            eq(List.of(entry,entry),calls);eq(true,calls.get(0)==calls.get(1));
            calls.clear();
            try { new RetryingSink(value->{calls.add(value);if(calls.size()==1)throw first;throw second;}).write(entry);throw new AssertionError("Expected second IOException"); }
            catch(IOException error) { eq(second,error);eq(List.of(first),Arrays.asList(error.getSuppressed())); }
            eq(2,calls.size());
            calls.clear();
            try { new RetryingSink(value->{calls.add(value);throw new IllegalStateException("bad");}).write(entry);throw new AssertionError("Expected runtime failure"); }
            catch(IllegalStateException error) { eq("bad",error.getMessage()); }
            eq(1,calls.size());
        }
        fails(NullPointerException.class,"delegate",()->new RetryingSink(null));
        List<Entry> calls=new ArrayList<>();RetryingSink sink=new RetryingSink(calls::add);
        fails(NullPointerException.class,"entry",()->{try{sink.write(null);}catch(IOException error){throw new UncheckedIOException(error);}});eq(List.of(),calls);
    }
    public static void main(String[] args) throws IOException {
        String mode=args.length==0?"full":args[0];
        switch(mode) {
            case "baseline"->{baseline(new LegacyJournal());baseline(new DispatchJournal());}
            case "core"->core();
            case "transfer"->transfer();
            case "extension"->extension();
            case "full"->{core();transfer();extension();}
            default->throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+checks+" checks passed");
    }
}
