import java.util.*;

//OUTER CLASS
public class Car {

    String make;
    String model;
    ArrayList<Tire> tires = new ArrayList<>();

    //INNER CLASS -----------------------------------------------------
    private class Tire {
        private String type = "classic";
        private double pressure = 32.0;

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
        public void setType(String type) {
            this.type = type;
        }
        public void deflate() {
            pressure--;
        }
        public String toString() {
            return type + ", " + Double.toString(pressure) + " psi";
        }
    }
    //-----------------------------------------------------------------

    public Car(String make, String model) {
        this.make = make;
        this.model = model;

        for (int i = 0; i < 4; i++) {
            tires.add(new Tire());
        }
    }

    public String toString() {
        //The outer class has complete access to inner class variables and methods, even if declared private
        String result = "";
        result += "\nMake: " + make;
        result += "\nModel: " + model;
        result += "\nTires: ";
        for (Tire tire : tires) {
            result += "\n\t" + tire;
        }
        return result;
    }
}
