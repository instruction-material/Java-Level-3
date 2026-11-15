package transfer;
import java.util.*;
public final class SnapshotDiff {
    private SnapshotDiff() {}
    public static int firstDifference(List<String> before,List<String> after) {
        List<String> one=List.copyOf(Objects.requireNonNull(before,"before"));
        List<String> two=List.copyOf(Objects.requireNonNull(after,"after"));
        int limit=Math.min(one.size(),two.size());
        for(int n=0;n<limit;n++)if(!one.get(n).equals(two.get(n)))return n;
        return one.size()==two.size()?-1:limit;
    }
}
