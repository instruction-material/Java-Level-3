class Main {
    public static void main(String[] args) {

        HashMap<Integer, Character> ascii = new HashMap<>();
        ascii.insert(1, 'A');
        System.out.println(ascii.get(1));

        for (int i = 0; i < 150; i++) {
            ascii.insert(i, (char)i);
        }
        System.out.println(ascii.get(113));
        System.out.println(ascii);
    }
}
