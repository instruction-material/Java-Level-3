package app;
enum GuestRecipient implements Recipient {
    INSTANCE;
    public String label(){return "guest";}
    public String channel(){return "screen";}
}
