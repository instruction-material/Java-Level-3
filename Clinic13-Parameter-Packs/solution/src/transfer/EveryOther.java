package transfer;
import java.util.*;
public final class EveryOther {
    private EveryOther() {}
    public static List<Integer> select(List<Integer> values) {
        List<Integer> input=List.copyOf(Objects.requireNonNull(values,"values"));
        List<Integer> result=new ArrayList<>();
        for(int index=0;index<input.size();index+=2)result.add(input.get(index));
        return List.copyOf(result);
    }
}
