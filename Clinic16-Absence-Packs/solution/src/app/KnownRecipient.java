package app;
import api.Profile;
import java.util.Objects;
final class KnownRecipient implements Recipient {
    private final Profile profile;
    KnownRecipient(Profile profile){this.profile=Objects.requireNonNull(profile,"profile");}
    public String label(){return profile.name();}
    public String channel(){return profile.channel();}
}
