public class Student {
    private String name;
    private int ID;
    private boolean isPresent;
    private static int numStudents;

    public Student(String name, int ID, boolean isPresent) {
        this.name = name;
        this.ID = ID;
        this.isPresent = isPresent;
        numStudents++;
    }

    public Student(String name, int ID) {
        this.name = name;
        this.ID = ID;
        numStudents++;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    public boolean getIsPresent() {
        return isPresent;
    }

    public static int getNumStudents() {
        return numStudents;
    }

    public void setID(int i) {
        ID = i;
    }

    public void setPresent(boolean p) {
        isPresent = p;
    }

    public String toString() {
        if (isPresent) {
            return name + " with the ID #" + ID + " is present in class today.";
        }
        return name + " with ID #" + ID + " is NOT present in class today.";
    }
}
