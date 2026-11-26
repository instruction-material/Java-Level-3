class Main {
    public static void main(String[] args) {
        var data = new HashMap<Integer, String>();
        data.put(0, "zero");
        data.put(47, "collision");
        data.put(-47, "negative");
        data.remove(0);
        System.out.println(data.has(0));
        System.out.println(data.get(47));
        System.out.println(data.get(-47));
    }
}
