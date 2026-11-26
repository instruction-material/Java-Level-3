import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    public static void main(String[] args) throws Exception {
        var table = new HashMap<Integer,String>();
        table.put(0, "first");
        table.put(47, "second");
        table.put(94, "third");
        table.remove(0);
        same(false, table.has(0));
        same("second", table.get(47));
        same("third", table.get(94));
        table.remove(94);
        table.remove(94);
        same("second", table.get(47));
        table.put(47, "updated");
        same("updated", table.get(47));
        for (int key = -100; key <= 100; key++) table.put(key * 47, "entry" + key);
        for (int key = -100; key <= 100; key++) same("entry" + key, table.get(key * 47));
        same(201L, table.toString().lines().count());
        table.put(Integer.MIN_VALUE, null);
        same(true, table.has(Integer.MIN_VALUE));
        table.remove(Integer.MIN_VALUE);
        same(false, table.has(Integer.MIN_VALUE));
        System.out.println("Reference lab checks passed");
    }
}
