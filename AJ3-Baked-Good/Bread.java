
public class Bread extends BakedGood implements ForSale {
    // small, medium, or large
    public Size loafSize;

    public Bread(String name, String[] ingredients, String flavor,
                 int batchSize, boolean forSale, Size loafSize) {
        super(name, ingredients, flavor, batchSize, forSale);
        this.loafSize = loafSize;
    }
    public double getPrice() {
        if (loafSize == Size.SMALL) {
            return 2.50;
        } else if (loafSize == Size.MEDIUM) {
            return 3.50;
        } else {
            return 4.50;
        }
    }

    public enum Size { SMALL, MEDIUM, LARGE }
}
