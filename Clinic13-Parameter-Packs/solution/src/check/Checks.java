package check;
import java.util.*;
public final class Checks {
    private static int count;
    private Checks() {}
    public static int count() { return count; }
    public static void yes(boolean value,String name) {
        count++;if(!value)throw new AssertionError(name);
    }
    public static void same(Object expected,Object actual,String name) {
        yes(Objects.equals(expected,actual),name+" expected="+expected+" actual="+actual);
    }
    public static void fails(Class<? extends RuntimeException> kind,String message,Runnable work) {
        count++;
        try{work.run();throw new AssertionError("missing "+kind.getSimpleName()+":"+message);}
        catch(RuntimeException failure) {
            if(failure.getClass()!=kind||!Objects.equals(message,failure.getMessage()))
                throw new AssertionError("wrong failure",failure);
        }
    }
    public static void immutable(List<?> list) {
        count++;try{list.clear();throw new AssertionError("mutable list");}
        catch(UnsupportedOperationException expected){ /* expected */ }
    }
}
