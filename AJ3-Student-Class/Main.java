class Main {
    public static void main(String[] args) {
        Student emily = new Student("Emily", 209699, true);
        Student rachel = new Student("Rachel", 475622, false);
        Student eric = new Student("Eric", 700000, true);
        Student andrew = new Student("Andrew", 101010, true);
        Student kelly = new Student("Kelly", 548221, false);

        System.out.println();
        System.out.println(emily);
        System.out.println(rachel);
        System.out.println(eric);
        System.out.println(andrew);
        System.out.println(kelly);
        System.out.println("\nThere are " + Student.getNumStudents() +
                           " students!");
    }
}
