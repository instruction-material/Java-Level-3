# Refactoring Clinic 16: Make absence policy explicit without hiding invalid data

The starter's baseline and core behavior checks already pass. A step log and actual source changes establish the design work. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    Hello, guest|screen
    Hello, Ada|email

## Preserve a deliberate missing-profile policy

The old Greetings API accepts a nullable Profile. Null deliberately produces hello="Hello, guest" and channel="screen". A present profile produces "Hello, "+its exact name and its stored channel. A real profile named guest with channel email is still present and must retain email.

Profile is an immutable value. Validate name first: null throws NullPointerException("name"); blank, longer than 16 UTF-16 char units, or containing pipe/carriage-return/newline throws IllegalArgumentException("name"). Accepted spaces and Unicode are retained. Validate channel second: null throws NullPointerException("channel"), and anything except exact email or screen throws IllegalArgumentException("channel"). A partially populated or invalid profile fails during construction; it is never silently converted into a guest.

Trace both old methods for null and a present profile. Extract a label decision first while keeping the channel path stable. Then introduce an internal Recipient abstraction. The reference uses KnownRecipient for present data and GuestRecipient for the intentional default; Recipient.of is the one nullable boundary. GreetingService remains the same public facade.

The guest object's methods define the default policy. They do not eliminate the need to distinguish presence at an input boundary. Default behavior is appropriate only where absence is an accepted domain case. Keep invalid Profile construction loud. Use the exact old outputs to protect this distinction.

Core behavior checks already pass in the starter. Establish the source refactor with actual small changes, moved responsibility and an external unchanged client. Explain which repeated caller choices disappeared, where null is still interpreted, why guest is a policy rather than fabricated personal data, and one boundary where absence must instead be an error.

## Independent transfer: an explicit Optional return

Complete ProfileLookup.find(Map<String,Profile> directory,String key). Validate directory non-null with NullPointerException("directory"), then lookup key. Keys must match [a-z]{1,8}; null key throws NullPointerException("key") and other invalid lookup keys throw IllegalArgumentException("key").

Validate and copy the complete directory in its iteration order before looking up a result. Directory keys use the same pattern, with messages "entryKey"; null keys throw NullPointerException and malformed ones IllegalArgumentException. Null profile values throw NullPointerException("profile"). Valid immutable Profile values are retained. Keep the supplied map unchanged.

Return Optional.of(profile) for a present key and Optional.empty() for a missing key. A missing entry and an invalid stored entry are different conditions. Even an invalid entry under an unrelated key is rejected by this complete-directory boundary. An empty directory is valid.

Retain present, missing, empty and malformed-directory cases. Optional represents an explicit no-result return; the Optional object itself is always non-null. This task is independent of the core Recipient classes.

## Independent extension: require presence

Complete StrictGreeting.hello(Optional<Profile> result). A null Optional throws NullPointerException("result"). An empty Optional throws NoSuchElementException("profile"). A present profile returns "Hello, "+its exact name.

This separate operation has a strict presence policy; keep the original nullable Greetings API's guest policy unchanged. Retain a present profile, a real profile named guest, an empty Optional failure and a null-container failure. Explain why this boundary should refuse absence rather than fabricate a default.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import app.GreetingService;
    import api.*;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            Greetings greetings=new GreetingService();
            if(!greetings.channel(null).equals("screen"))throw new AssertionError("absent");
            if(!greetings.channel(new Profile("guest","email")).equals("email"))throw new AssertionError("present guest");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
