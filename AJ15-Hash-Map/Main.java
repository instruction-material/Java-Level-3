import java.util.ArrayList;

class Main {
  public static void main(String[] args) {

    //Bakery HashMap
    HashMap<String, String> bakery = new HashMap<>();
    System.out.println("\nBAKERY HASHMAP:");
    bakery.insert("Katie", "Manager");
    bakery.insert("Amy", "Baker");
    bakery.insert("Sahana", "Baker");
    bakery.insert("Varun", "Cashier");
    bakery.insert("Sarah", "Cashier");
    bakery.insert("Kurt", "Supervisor");
    System.out.println(bakery);
    bakery.remove("Amy");
    System.out.println("Removed Amy");
    bakery.put("Sarah", "Baker");
    System.out.println("Changed Sarah's position to baker");
    System.out.println(bakery);
    System.out.println("Does the bakery employ Amy? " + bakery.has("Amy"));
    System.out.println("Sarah's job: " + bakery.get("Sarah"));

    //ASCII HashMap
    System.out.println("\nASCII HASHMAP:");
    HashMap<Integer, Character> ascii = new HashMap<>();
    for(int i = 0; i < 150; i++){
      ascii.insert(i,(char)i);
    }
    System.out.println("\n"+ascii);
  }
}