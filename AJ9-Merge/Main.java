import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

class Main {
    public static void main(String[] args) {

        int size = 5;
        Random random = new Random();
        ArrayList<Integer> listA = new ArrayList<>();
        ArrayList<Integer> listB = new ArrayList<>();

        //add random numbers
        for (int i = 0; i < size; i++) {
            listA.add(random.nextInt(100));
            listB.add(random.nextInt(100));
        }
        Collections.sort(listA);
        Collections.sort(listB);

        //print, merge, and print again
        System.out.println("\n" + listA);
        System.out.println(listB);
        ArrayList<Integer> listC = merge(listA, listB);
        System.out.println(listC);
    }
    public static ArrayList<Integer> merge(ArrayList<Integer> a,
                                           ArrayList<Integer> b) {

        // Move a cursor past each copied value and keep both input lists intact.
        ArrayList<Integer> result = new ArrayList<>(a.size() + b.size());
        int left = 0;
        int right = 0;
        while (left < a.size() && right < b.size()) {
            Integer fromLeft = a.get(left);
            Integer fromRight = b.get(right);
            if (fromLeft <= fromRight) {
                result.add(fromLeft);
                left++;
            } else {
                result.add(fromRight);
                right++;
            }
        }
        result.addAll(a.subList(left, a.size()));
        result.addAll(b.subList(right, b.size()));
        return result;
    }
}
