import java.util.List;
import java.util.Locale;
import java.util.function.Function;
public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected){checks++;if(!java.util.Objects.equals(actual,expected))throw new AssertionError(actual+" != "+expected);}
    private static void fails(Class<? extends Throwable> type,Runnable work){
        checks++;try{work.run();}catch(Throwable error){if(type.isInstance(error))return;throw new AssertionError(error);}
        throw new AssertionError(type.getSimpleName());
    }
    public static void main(String[] args){
        Main.Pluggable injected=new Main.Pluggable(text->"NOTICE: "+text.trim().toUpperCase(Locale.ROOT));
        List<Function<String,String>> versions=List.of(Main.Legacy::banner,Main.Legacy::email,Main.Direct::banner,Main.Direct::email,injected::banner,injected::email);
        for(String text:List.of(""," hi ","MiXeD","tab\t spaced")){
            String expected="NOTICE: "+text.trim().toUpperCase(Locale.ROOT);
            for(Function<String,String> version:versions)equal(version.apply(text),expected);
        }
        for(Function<String,String> version:versions){
            fails(NullPointerException.class,()->version.apply(null));
            fails(IllegalArgumentException.class,()->version.apply("a\nb"));
            fails(IllegalArgumentException.class,()->version.apply("a\rb"));
        }
        fails(NullPointerException.class,()->new Main.Pluggable(null));
        fails(NullPointerException.class,()->new Main.Pluggable(text->null).banner("ok"));
        fails(IllegalArgumentException.class,()->new Main.Pluggable(text->"a\nb").email("ok"));
        equal(new Main.Pluggable(text->"custom:"+text).banner("keep"),"custom:keep");
        equal(Main.Direct.banner("keep"),"NOTICE: KEEP");
        int[] calls={0};
        equal(new Main.Pluggable(text->{calls[0]++;return text;}).banner("once"),"once");
        equal(calls[0],1);
        System.out.println("Judgment worked contracts: "+checks+" passed");
    }
}
