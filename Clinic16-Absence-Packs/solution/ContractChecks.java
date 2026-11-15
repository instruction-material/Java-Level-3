import api.*;
import app.GreetingService;
import legacy.LegacyGreetings;
import transfer.ProfileLookup;
import extension.StrictGreeting;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void behavior(Supplier<Greetings> make) {
        Greetings greetings=make.get();
        same("Hello, guest",greetings.hello(null),"absent greeting");
        same("screen",greetings.channel(null),"absent channel");
        for(String name:List.of("Ada"," guest ","guest","é","x".repeat(16)))for(String channel:List.of("email","screen")) {
            Profile profile=new Profile(name,channel);
            same("Hello, "+name,greetings.hello(profile),"present exact name");
            same(channel,greetings.channel(profile),"present exact channel");
            same(name,profile.name(),"input unchanged");
        }
        Profile namedGuest=new Profile("guest","email");
        same("email",greetings.channel(namedGuest),"present guest is not absence");
        fails(NullPointerException.class,"name",()->new Profile(null,null));
        for(String bad:List.of(""," ","bad\n","bad|","x".repeat(17)))
            fails(IllegalArgumentException.class,"name",()->new Profile(bad,null));
        fails(NullPointerException.class,"channel",()->new Profile("Ada",null));
        fails(IllegalArgumentException.class,"channel",()->new Profile("Ada","sms"));
        same("Hello, guest",make.get().hello(null),"independent default");
    }
    private static void transfer() {
        Profile ada=new Profile("Ada","email");Map<String,Profile> entries=new LinkedHashMap<>();entries.put("ada",ada);
        same(Optional.of(ada),ProfileLookup.find(entries,"ada"),"present Optional");
        same(Optional.empty(),ProfileLookup.find(entries,"missing"),"absent Optional");
        same(Map.of("ada",ada),entries,"directory retained");
        fails(NullPointerException.class,"directory",()->ProfileLookup.find(null,null));
        fails(NullPointerException.class,"key",()->ProfileLookup.find(Map.of(),null));
        fails(IllegalArgumentException.class,"key",()->ProfileLookup.find(Map.of(),"BAD"));
        fails(IllegalArgumentException.class,"key",()->ProfileLookup.find(Map.of(),"toolongid"));
        Map<String,Profile> bad=new LinkedHashMap<>();bad.put(null,ada);
        fails(NullPointerException.class,"entryKey",()->ProfileLookup.find(bad,"ada"));
        bad.clear();bad.put("BAD",ada);
        fails(IllegalArgumentException.class,"entryKey",()->ProfileLookup.find(bad,"missing"));
        bad.clear();bad.put("broken",null);
        fails(NullPointerException.class,"profile",()->ProfileLookup.find(bad,"missing"));
        same(Optional.empty(),ProfileLookup.find(Map.of(),"ada"),"valid empty directory");
    }
    private static void extension() {
        same("Hello, Ada",StrictGreeting.hello(Optional.of(new Profile("Ada","email"))),"required present");
        same("Hello, guest",StrictGreeting.hello(Optional.of(new Profile("guest","email"))),"real guest name retained");
        fails(NullPointerException.class,"result",()->StrictGreeting.hello(null));
        fails(NoSuchElementException.class,"profile",()->StrictGreeting.hello(Optional.empty()));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyGreetings::new);behavior(GreetingService::new);}
            case "core" -> behavior(GreetingService::new);
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {behavior(GreetingService::new);transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
