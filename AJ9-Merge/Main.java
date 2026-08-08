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

        //compare the first elements of the two lists,
        //move the smaller of the two to the "result" list
        //repeat until one of the lists is empty
        ArrayList<Integer> result = new ArrayList<Integer>();
        while (a.size() != 0 && b.size() != 0) {
            if (a.get(0) < b.get(0)) {
                result.add(a.get(0));
                a.remove(0);
            } else {
                result.add(b.get(0));
                b.remove(0);
            }
        }

        //one list will run out of elements before the other,
        //so add the rest of the remaining elements
        if (a.size() == 0) {
            result.addAll(b);
        }
        if (b.size() == 0) {
            result.addAll(a);
        }

        return result;
    }
}
