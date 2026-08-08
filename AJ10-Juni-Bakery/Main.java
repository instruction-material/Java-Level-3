class Main {
    public static void main(String[] args) {

        Bakery juniBakery = new Bakery("Juni Bakery", "Juni Street");

        System.out.println();
        juniBakery.hireWorkers(2);
        juniBakery.addBakedGoods(2);
        System.out.println(juniBakery);
    }
}
