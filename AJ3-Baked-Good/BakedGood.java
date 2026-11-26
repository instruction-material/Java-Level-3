public abstract class BakedGood implements Comparable<BakedGood> {
    private String name;
    private final String[] ingredients;
    private String flavor;
    private int batchSize;
    private boolean forSale;

    public BakedGood(String name, String[] ingredients, String flavor,
                     int batchSize, boolean forSale) {
        this.name = name;
        this.ingredients = ingredients.clone();
        this.flavor = flavor;
        this.batchSize = batchSize;
        this.forSale = forSale;
    }

    public String getFlavor() {
        return flavor;
    }

    public boolean getForSale() {
        return forSale;
    }

    public int getNumIngredients() {
        return ingredients.length;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int compareTo(BakedGood bg) {
        return Integer.compare(getNumIngredients(), bg.getNumIngredients());
    }

    public String toString() {
        return "The baked good is " + name + ". It has " + ingredients.length +
            " ingredients and is " + flavor;
    }

    public abstract double getPrice();
}
