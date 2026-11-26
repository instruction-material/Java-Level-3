import java.util.*;

public class ListGenerator {

    private static final Random random = new Random();

    public static ArrayList<Integer> getRandom(int size, int range) {
        return getRandom(size, range, random);
    }

    public static ArrayList<Integer> getRandom(int size, int range, Random source) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nums.add(source.nextInt(range));
        }
        return nums;
    }
}
