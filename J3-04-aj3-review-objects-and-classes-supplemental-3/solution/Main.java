import java.util.*;

class Main {
    static abstract class Parcel {
        final int weight;
        Parcel(int weight) {
            if (weight < 0) throw new IllegalArgumentException("Negative weight");
            this.weight = weight;
        }
        abstract long feeCents();
    }
    static class Standard extends Parcel {
        Standard(int weight) { super(weight); }
        long feeCents() {
            return 100 + 25L * weight;
        }
    }
    static class Express extends Parcel {
        Express(int weight) { super(weight); }
        long feeCents() {
            return 100 + 50L * weight;
        }
    }
    public static void main(String[] args) {
        List<Parcel> parcels = List.of(new Standard(2), new Express(2));
        parcels.forEach(parcel -> System.out.println(parcel.feeCents()));
    }
}
