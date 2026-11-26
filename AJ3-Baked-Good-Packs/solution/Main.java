import java.util.*;

class Main {
    interface ForSale {
        int priceCents();
    }

    static abstract class BakedGood implements ForSale, Comparable<BakedGood> {
        final String name;
        private final List<String> ingredients;

        BakedGood(String name, List<String> ingredients) {
            this.name = name;
            this.ingredients = List.copyOf(ingredients);
        }

        int ingredientCount() {
            return ingredients.size();
        }

        @Override
        public int compareTo(BakedGood other) {
            return Integer.compare(ingredientCount(), other.ingredientCount());
        }
    }

    enum Size { SMALL, MEDIUM, LARGE }

    static class Bread extends BakedGood {
        final Size size;

        Bread(String name, List<String> ingredients, Size size) {
            super(name, ingredients);
            this.size = Objects.requireNonNull(size);
        }

        public int priceCents() {
            return switch (size) {
                case SMALL -> 250;
                case MEDIUM -> 350;
                case LARGE -> 450;
            };
        }
    }

    public static void main(String[] args) {
        ForSale bread = new Bread("Wheat", List.of("flour", "water"), Size.MEDIUM);
        System.out.println(bread.priceCents() + " cents");
    }
}
