import java.util.Map;

import java.util.*;
class Main {
    public static void main(String[] args) {

        // 1) Creating a Map
        //use TreeMap for ordered maps
        //use HashMap for unordered maps (it's slightly faster)
        Map<String, Double> prices = new TreeMap<>();

        // 2) Put
        prices.put("apple", 0.99);
        prices.put("bread", 3.75);
        prices.put("milk", 2.49);
        prices.put("cereal", 4.25);
        prices.put("eggs", 1.50);

        // 3) Get
        System.out.println("\nAn apple costs: " + prices.get("apple"));

        // 4) ContainsKey
        if (prices.containsKey("eggs")) {
            System.out.println("We have eggs!");
            System.out.println("Eggs cost: " + prices.get("eggs"));
        } else {
            System.out.println("Sorry we're out of eggs.");
        }

        // 5) Remove
        prices.remove("apple");

        // 6) KeySet
        for (String item : prices.keySet()) {
            System.out.print("\n" + item + ": ");
            System.out.print(prices.get(item));
        }

        // 7) Values
        System.out.println("\n\nHere are just the prices:");
        for (double price : prices.values()) {
            System.out.println(price);
        }
    }
}
