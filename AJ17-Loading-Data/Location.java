public class Location implements Comparable<Location> {

    String name;
    double latitude;
    double longitude;
    double distance;
    Location previous;

    Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0.0;
        this.previous = null;
    }
    public double getLat() {
        return latitude;
    }
    public double getLong() {
        return longitude;
    }

    public Location getPrevious() {
        return previous;
    }
    public void setPrevious(Location previous) {
        this.previous = previous;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public int compareTo(Location that) {
        double difference = this.getDistance() - that.getDistance();
        if (difference < 0) {
            return -1;
        } else if (difference > 0) {
            return 1;
        }
        return 0;
    }
}
