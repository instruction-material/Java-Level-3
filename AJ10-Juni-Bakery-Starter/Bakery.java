import java.util.*;

public class Bakery {
    private String name;
    private String address;
    private ArrayList<BakedGood> bakedGoods = new ArrayList<>();
    private ArrayList<Worker> workers = new ArrayList<>();

    public Bakery(String n, String a) {
        name = n;
        address = a;
    }

    // NESTED BAKEDGOODS CLASS ----------------------------------------
    private final class BakedGood {
        private String name;
        private String[] ingredients;
        private int numIngredients;
        private String flavour;
        private int batchSize;

        public BakedGood(String n, String[] i, int ni, String f, int b) {
            name = n;
            ingredients = i;
            numIngredients = ni;
            flavour = f;
            batchSize = b;
        }

        public String getFlavour() {
            return flavour;
        }

        public int getNumIngredients() {
            return numIngredients;
        }

        public void setBatchSize(int b) {
            batchSize = b;
        }

        private String returnIngredients() {
            String ingredientsList = "";

            for (int i = 0; i < numIngredients; i++) {
                if (i < numIngredients - 1) {
                    ingredientsList += ingredients[i] + ", ";
                } else {
                    ingredientsList += ingredients[i];
                }
            }
            return ingredientsList;
        }

        public String toString() {
            return name + "\n\tFlavour: " + flavour +
                "\n\tIngredients: " + returnIngredients();
        }
    }

    // NESTED WORKER CLASS --------------------------------------------
    private final class Worker {
        private String name;
        private String position;
        private boolean employeeOfTheMonth;
        private double salary;

        public Worker(String n, String p, boolean e, double s) {
            name = n;
            position = p;
            employeeOfTheMonth = e;
            salary = s;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        public void giveRaise(double raiseIncrease) {
            salary += raiseIncrease;
        }

        public boolean isEmployeeOfTheMonth() {
            return employeeOfTheMonth;
        }

        public void makeEmployeeOfTheMonth() {
            employeeOfTheMonth = true;
        }

        public String getPosition() {
            return position;
        }

        public void changePosition(String p) {
            position = p;
        }

        public String toString() {
            String s = "Name: " + name + "\n\tPosition: " + position +
                       "\n\tSalary: $" + String.format("%.2f", salary) +
                       " per hour";

            if (employeeOfTheMonth) {
                s += "\n\tEmployee of the Month";
            }

            return s;
        }
    }

    //PROVIDED METHODS ------------------------------------------------
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getNumWorkers() {
        return workers.size();
    }

    //BAKERY METHODS --------------------------------------------------

    /**
  * creates the given number of Worker objects and adds them to the workers ArrayList
  * @param numWorkers - the number of Worker objects to create
  **/
    public void hireWorkers(int numWorkers) {
        //TODO
    }
    /**
  * creates the given number of BakedGood objects and adds them to the bakedGoods ArrayList
  * @param numBakedGoods - the number of BakedGood objects to create
  **/
    public void addBakedGoods(int numBakedGoods) {
        //TODO
    }
    public String toString() {
        //TODO
        return "";
    }
}
