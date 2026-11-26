import java.util.*;

class Main {
    static OptionalInt count(Scanner input) {
        // TODO: Read through invalid lines to the first count.
        throw new UnsupportedOperationException("Read through invalid lines to the first count");
    }
    public static void main(String[] args) {
        System.out.println(count(new Scanner("bad\n-3\n 7 \n8")).orElseThrow());
    }
}
