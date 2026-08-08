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

    //BAKERY METHODS --------------------------------------------------

    public void hireWorkers(int numWorkers) {

        Scanner s = new Scanner(System.in);
        System.out.println("We are currently hiring " + numWorkers +
                           " workers.");
        for (int i = 0; i < numWorkers; i++) {

            //name
            System.out.print("Please enter worker " + (i + 1) + "'s name: ");
            String workerName = s.nextLine();

            //position
            System.out.print("Please enter worker " + (i + 1) +
                             "'s position: ");
            String workerPosition = s.nextLine();

            //employee of the month
            System.out.print("Is worker " + (i + 1) +
                             " employee of the month? (Y/N): ");
            String eofm = s.nextLine();
            boolean employeeOfTheMonth = false;
            if (eofm.equals("Y")) {
                employeeOfTheMonth = true;
            }

            //salary
            System.out.print("Please enter worker " + (i + 1) + "'s salary: ");
            double workerSalary = s.nextDouble();
            s.nextLine();

            //add the new worker
            Worker w = new Worker(workerName, workerPosition,
                                  employeeOfTheMonth, workerSalary);
            workers.add(w);

            System.out.println();
        }
    }

    public void addBakedGoods(int numBakedGoods) {
        Scanner s = new Scanner(System.in);
        System.out.println("We are currently baking " + numBakedGoods +
                           " items.");

        for (int i = 0; i < numBakedGoods; i++) {

            //name
            System.out.print("Please enter the name of the baked good: ");
            String bakedGoodName = s.nextLine();

            //flavor
            System.out.print("Please enter the flavour of the baked good: ");
            String flavour = s.nextLine();

            //ingredients
            System.out.print(
                "Please enter the number of ingredients in the item: ");
            int numIngredients = s.nextInt();
            s.nextLine();
            String[] ingredients = new String[numIngredients];
            for (int j = 0; j < numIngredients; j++) {
                System.out.print("Please enter ingredient " + (j + 1) + ": ");
                String ingredient = s.nextLine();
                ingredients[j] = ingredient;
            }

            //batch size
            System.out.print("Please enter the batch size in the item: ");
            int batchSize = s.nextInt();
            s.nextLine();

            //add the new baked good
            BakedGood b = new BakedGood(bakedGoodName, ingredients,
                                        numIngredients, flavour, batchSize);
            bakedGoods.add(b);

            System.out.println();
        }
    }

    public String toString() {
        return "Welcome to the Juni Bakery!\nWe currently employ " +
            workers.size() +
            " very qualified workers.\nBelow is our employee roster: \n" +
            employeeRoster() +
            "\nOur menu consists of the following delicious baked goods:\n" +
            bakedGoodsMenu();
    }
    public String bakedGoodsMenu() {
        String menu = "";
        for (BakedGood b : bakedGoods) {
            menu += "\n" + b.toString() + "\n";
        }
        return menu;
    }
    public String employeeRoster() {
        String roster = "";
        for (Worker w : workers) {
            roster += "\n" + w.toString() + "\n";
        }
        return roster;
    }

    // Methods of the Bakery Class
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getNumWorkers() {
        return workers.size();
    }
}
