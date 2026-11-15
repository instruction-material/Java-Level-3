import api.*;
import app.EnergyMeter;
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static void main(String[] args) {
        EnergyAPI client=new EnergyMeter();
        if(!client.measure(33,3,50).equals(new EnergyResult(99,49,50)))throw new AssertionError("energy");
        System.out.println("compatibility passed");
    }
}
