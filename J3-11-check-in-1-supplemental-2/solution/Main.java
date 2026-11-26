public class Main {
    static int firstPassing(boolean[] trials) {
        int lower = 0;
        int upper = trials.length;
        while (lower < upper) {
            int middle = lower + (upper - lower) / 2;
            if (trials[middle]) upper = middle;
            else lower = middle + 1;
        }
        return lower == trials.length ? -1 : lower;
    }

    public static void main(String[] args) {
        System.out.println(firstPassing(new boolean[]{false, false, true, true}));
        System.out.println(firstPassing(new boolean[]{false, false}));
    }
}
