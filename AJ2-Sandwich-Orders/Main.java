import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        //initialize the ingredient list
        //note that no two ingredients have the same first letter
        ArrayList<String> ingredientList = new ArrayList<String>();
        ingredientList.add("lettuce");
        ingredientList.add("tomatoes");
        ingredientList.add("onions");
        ingredientList.add("cheese");
        ingredientList.add("ketchup");
        ingredientList.add("pickles");
        ingredientList.add("mayonnaise");

        ArrayList<String> order = getOrder(ingredientList);
        System.out.println(orderConverter(order));
    }

    //returns an ArrayList with the ingredients the user wants
    public static ArrayList<String> getOrder(ArrayList<String> ingredientList) {
        Scanner in = new Scanner(System.in);

        System.out.println(
            "\nWelcome to Juni Eats. Please type 'YES' if you would like an "
            + "ingredient in your sandwich. Type 'NO' otherwise. ");

        ArrayList<String> order = new ArrayList<String>();
        for (int i = 0; i < ingredientList.size(); i++) {
            System.out.print("Would you like " + ingredientList.get(i) +
                             " in you sandwich? ");
            String response = in.nextLine();
            if (response.equals("YES")) {
                order.add(ingredientList.get(i));
            }
        }
        return order;
    }

    //converts an order into an abbreviated string
    public static String orderConverter(ArrayList<String> order) {
        String abbreviation = "";
        for (int i = 0; i < order.size(); i++) {
            abbreviation += order.get(i).charAt(0);
        }
        return abbreviation;
    }
}
