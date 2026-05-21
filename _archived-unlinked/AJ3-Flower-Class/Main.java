class Main {
  public static void main(String[] args) {

    //CREATE the flowers
    Flower daisy = new Flower();
    Flower magnolia = new Flower("Magnolia",8,"white",4.5,false);
    Flower sunflower = new Flower("Sunflower","yellow",2.5);

    daisy.setName("Daisy");
    daisy.setPetals(8);
    daisy.setColor("white");
    daisy.setMaintenanceLevel(0.5);
    daisy.setIsPerennial(false);
    
    //PRINT the flowers
    System.out.println("\n" + daisy);

    System.out.println("\n" + magnolia.getName());
    System.out.println(magnolia.getNumPetals());
    System.out.println(magnolia.getColor());
    System.out.println(magnolia.getMaintenanceLevel());

    System.out.println("\n" + sunflower);

  }
}