package app;
import api.Profile;
interface Recipient {
    String label();
    String channel();
    static Recipient of(Profile profile) {
        return profile==null?GuestRecipient.INSTANCE:new KnownRecipient(profile);
    }
}
