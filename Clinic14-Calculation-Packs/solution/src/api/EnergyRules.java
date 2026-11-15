package api;
public final class EnergyRules {
    private EnergyRules() {}
    public static void watts(int value) {if(value<0||value>1000)throw new IllegalArgumentException("watts");}
    public static void seconds(int value) {if(value<0||value>3600)throw new IllegalArgumentException("seconds");}
    public static void efficiency(int value) {if(value<0||value>100)throw new IllegalArgumentException("efficiency");}
}
