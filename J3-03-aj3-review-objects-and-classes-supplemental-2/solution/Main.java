import java.util.*;

class Main {
    static class Shelf {
        private int copies;

        Shelf(int copies) {
            if (copies < 0) throw new IllegalArgumentException("Negative stock");
            this.copies = copies;
        }

        boolean checkout() {
            if (copies == 0) return false;
            copies--;
            return true;
        }

        int copies() { return copies; }
    }
    public static void main(String[] args) {
        var shelf = new Shelf(1);
        System.out.println(shelf.checkout());
        System.out.println(shelf.checkout());
        System.out.println(shelf.copies());
    }
}
