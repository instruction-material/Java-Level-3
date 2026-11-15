package extension;
import java.util.*;
public final class MergeRanges {
    private MergeRanges() {}
    public static List<Integer> unique(List<Integer> first,List<Integer> second) {
        List<Integer> one=List.copyOf(Objects.requireNonNull(first,"first"));
        List<Integer> two=List.copyOf(Objects.requireNonNull(second,"second"));
        Set<Integer> merged=new TreeSet<>(one);merged.addAll(two);
        return List.copyOf(merged);
    }
}
