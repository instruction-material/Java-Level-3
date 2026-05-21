public class Flower {
  // instance variables
  private String name;
  private int numPetals;
  private String color;
  private double maintenanceLevel;
  private boolean isPerennial;

  // constructors
  public Flower() {
    this.name = "";
    this.numPetals = 0;
    this.color = "";
    this.maintenanceLevel = 0.0;
    this.isPerennial = false;
  }

  public Flower(String name, int numPetals, String color, double maintenanceLevel, boolean isPerennial) {
    this.name = name;
    this.numPetals = numPetals;
    this.color = color;
    this.maintenanceLevel = maintenanceLevel;
    this.isPerennial = isPerennial;
  }

  public Flower(String name, String color, double maintenanceLevel) {
    this.name = name;
    this.color = color;
    this.maintenanceLevel = maintenanceLevel;
  }

  // getter methods
  public String getName() {
    return name;
  }
  public int getNumPetals(){
    return numPetals;
  }
  public String getColor(){
    return color;
  }
  public double getMaintenanceLevel(){
    return maintenanceLevel;
  }

  // setter methods
  public void setPetals(int numPetals) {
    this.numPetals = numPetals;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setName(String name){
    this.name = name;
  }
  public void setMaintenanceLevel(double level){
    this.maintenanceLevel = level;
  }
  public void setIsPerennial(boolean isPerennial){
    this.isPerennial = isPerennial;
  }

  // toString() method
  public String toString() {
    return "Flower name: " + name + "\nNumber of Petals: " + numPetals + "\nColor: " + color + "\nMaintenance Level: " + maintenanceLevel + "\nPerennial Plant: " + isPerennial;
  }

}