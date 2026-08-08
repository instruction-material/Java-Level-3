class Main {
    public static void main(String[] args) {
        String[] breadIngredients = {"flour", "water", "yeast"};
        String[] pumpkinBreadIngredients = {
            "flour",         "water",   "sugar",  "eggs",    "baking soda",
            "baking powder", "pumpkin", "butter", "cinnamon"};

        System.out.println();
        Bread bread = new Bread("Basic Bread", breadIngredients, "savory", 12,
                                true, Bread.Size.LARGE);
        System.out.println(bread);

        Bread pumpkinBread = new Bread("Pumpkin Bread", pumpkinBreadIngredients,
                                       "sweet", 13, true, Bread.Size.SMALL);
        System.out.println(pumpkinBread);
    }
}
