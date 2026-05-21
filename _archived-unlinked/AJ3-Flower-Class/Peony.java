// example of a subclass
public class Peony extends Flower {
  String type;

  public Peony(String name, int numPetals, String color, double maintenanceLevel, String type) {
    super(name,numPetals,color,maintenanceLevel,true);
    this.type = "peony";
  }

}