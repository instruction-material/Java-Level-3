class Main {
    public static void main(String[] args) {

        Bakery juniBakery = new Bakery("Juni Bakery", "Juni Street");

        juniBakery.hireWorkers(1);
        juniBakery.addBakedGoods(1);
        System.out.println(juniBakery);
    }
}
