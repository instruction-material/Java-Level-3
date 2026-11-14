# DPR6: Simplifying method calls

A parcel desk exposes a method named price that also records a dispatch. Its boolean and unused string obscure intent; its setter changes future charges; bad parcel input returns -1 and missing lookup returns null. The modern API makes those choices explicit while a compatibility boundary preserves existing callers.

The supplied legacy program, client and checks are runnable. The starter's quote, send, compatibility price and optional printNext methods are unfinished. Complete the three required methods before the optional queue task.

## Required project

Edit src/dispatch/DispatchService.java and src/dispatch/CompatibilityDesk.java. Keep LegacyDesk.java, ClientCompatibility.java, supplied value types, log and checks unchanged. Record predictions, commands and design decisions in REFACTORING.md.

### Charge and state contract

All money is integer cents. A Parcel contains its exact label, grams and zone. Accept nonblank labels, grams 1–5000 and zones 1–3. Preserve spaces, punctuation and case in an accepted label. A null label throws NullPointerException("label"); a blank label throws IllegalArgumentException("label"). Invalid grams and zone throw IllegalArgumentException("grams") and IllegalArgumentException("zone") respectively. Validation order is label, grams, zone.

The number of weight bands is ceil(grams / 500). The charge is:

    zone * 100 + bands * 40 + speed surcharge - discountCents

STANDARD surcharge is 0; EXPRESS surcharge is 250. Discounts are integer cents from 0 through 100, inclusive. A 500g zone1 STANDARD parcel costs 140 without discount. A 501g zone2 EXPRESS parcel with discount25 costs 505. At 1000g the bands are still2; at1001g they become3.

DispatchService.standard(log) selects discount0. discounted(discountCents,log) selects a fixed discount. Both validate required log first; a missing log throws NullPointerException("log"), including when discount is invalid. Bad discount throws IllegalArgumentException("discountCents").

quote(parcel,speed) returns a Quote(label,cents) without writing the log. Repeated quotes have the same value and leave total/history unchanged. Validate parcel before speed, throwing NullPointerException("parcel") or NullPointerException("speed"). Derive the surcharge from speed inside this method; callers do not supply that redundant value.

send(parcel,speed) obtains one quote, appends it once and returns it. Repeated sends create repeated records, including duplicate labels. A failed validation appends nothing. Returning the result of this command helps a caller identify what was sent; the method still has an observable effect.

The separate DispatchLog owns state. snapshot() returns an immutable copy. A previously returned snapshot never changes. totalCents() includes every successful send. find(label) returns the latest matching Quote in an Optional, or Optional.empty() for a valid label with no record. Null and blank lookup labels remain errors. Optional itself is always non-null.

A service has no setting method for its fixed discount. To change policy, create another service using the same log. Earlier records stay, and the old service keeps its own original discount.

### Existing caller contract

CompatibilityDesk retains its constructor(int), setDiscount(int), price(String,int,int,boolean,String), find(String), totalCents() and history() signatures. Constructor/setter reject discounts outside0–100. A failed setter leaves the earlier service usable.

price retains the original command behavior: one successful call records one dispatch and returns its cents. Its unused string accepts any value, including null. Invalid parcel input returns -1 and makes no write. Limit exception translation to Parcel construction; an unrelated service failure must remain visible. Convert the boolean to DeliverySpeed, then call send.

The old find returns null for absent, null or blank labels. Preserve that behavior at this boundary. Modern find has the separate Optional/error contract above. This is a deliberate new API contract with a source-compatible adapter, rather than a claim that changing null to Optional preserves every old call.

Do not remove the compatibility setter during this exercise. Its implementation selects a new fixed configuration sharing the old log. A future removal needs an inventory of external callers, a replacement API, a published deprecation interval and a deliberate compatibility decision. Recompiling the supplied client establishes source compatibility for those calls; it does not prove binary, reflection, serialization or every overload contract.

### Worked client

Run Main baseline before editing. Main core uses the preserved ClientCompatibility.java. Both completed modes print exactly:

    140
    505
    645
    -1
    Quote[label=B, cents=505]
    null

The first charge enters the log with discount0. The setter selects discount25 for later work without erasing the first record. The second charge is200+80+250-25. The invalid parcel adds no record. B is present; missing is absent.

For a modern client, construct a log, create a standard service and Parcel("A",500,1), and call quote twice. Both results equal Quote("A",140), with an empty log. Call send twice; history now contains two quotes and total is280. A snapshot taken after the first send still has one quote.

## Fourteen techniques

Each technique changes a particular responsibility. Use the smallest change that improves the call while preserving its stated boundary.

1. Rename Method: price hides its write. send names the command; quote names the pure calculation. Introduce the new name, migrate modern callers, and retain the old entry point while existing clients still require it.

2. Add Parameter: when a new client must select delivery speed, quote(parcel,speed) carries that necessary choice. During a source migration, an existing quote(parcel) could delegate with STANDARD. Adding a required argument without that bridge changes old call sites.

3. Remove Parameter: unused contributes nothing to calculation or validation. Remove it from the modern API. The old five-argument method keeps it for compatibility and accepts null.

4. Separate Query from Modifier: split reading a charge from recording a dispatch. A caller can preview twice without buying or logging twice. Keep send as the one operation that records. Renaming the original command to quote would leave its effect hidden.

5. Parameterize Method: standard and express charging share weight/zone arithmetic. One quote method takes DeliverySpeed and obtains the small surcharge variation from it. Separate algorithms would need a different design; this variation is one amount.

6. Introduce Parameter Object: replace the repeated label/grams/zone group with validated Parcel. It gives these values one meaning and validation boundary. A record groups data; the price rule still belongs to the service.

7. Preserve Whole Object: once a client has a Parcel, pass that object to quote instead of unpacking it into three primitives and rebuilding it. This differs from creating a new group for unrelated loose values. Passing an entire application context merely to read one unrelated field would create excess coupling.

8. Remove Setting Method: fixed service discount makes its configuration stable after construction. Change policy by selecting a new service. Mutable dispatch history still has one explicit owner. The compatibility setter survives only at the old boundary and preserves that history.

9. Replace Parameter with Explicit Methods: a factory such as configured(log,false) hides what false selects. Named standard(log) and discounted(cents,log) state two creation intents. Keep the actual discount amount as data; creating101 different named discount methods would add noise.

10. Replace Parameter with Method Call: a signature quote(parcel,speed,surcharge) lets callers pass contradictory speed and surcharge. Read speed.surchargeCents() inside quote, retaining one source of truth. This is safe here because the enum value is stable and the accessor is pure; repeatedly calling a changing remote source would need another contract.

11. Hide Method: DispatchLog.append is package-private. The dispatch service can record; unrelated clients use snapshot, find and total. Reducing visibility must account for existing consumers and tests. It does not make an exposed public method disappear safely by itself.

12. Replace Constructor with Factory Method: the service constructor is private and standard/discounted name how it is configured. These static creation functions need no subclass hierarchy or override. Their return type could later hide another implementation, provided the contract remains intact.

13. Replace Error Code with Exception: the modern typed boundary reports invalid data with specific exceptions instead of mixing -1 into a valid cents result. The old adapter still returns -1 for the original invalid-parcel cases. Caller migration must explicitly change its error handling before removing that adapter.

14. Replace Exception with Test: the optional queue client checks hasNext before remove when an empty queue is an ordinary outcome. It still preserves the queue's strict remove exception for callers who choose that operation. Check-then-act is suitable only for this queue's single-thread ownership; concurrent consumers need an atomic operation.

## Optional: queue absence and strict errors

Use a separate optional project and complete src/dispatch/optional/QueueClient.java. The supplied LabelQueue is FIFO and belongs to one thread. add requires a non-null Quote. remove throws NoSuchElementException("empty queue") when empty. hasNext and count are pure observations.

LegacyQueueClient catches the ordinary empty result. QueueClient should check hasNext, return Optional.empty() if empty, or remove one quote and return Optional.of(label + ":" + cents). It must require a non-null queue with NullPointerException("queue"). It must not return null or swallow required-argument errors.

Worked sequence: add Quote("A",140), then Quote("B",505). Three printNext calls return Optional[A:140], Optional[B:505], Optional.empty. Counts after those calls are1,0,0. Another empty call also returns Optional.empty and changes nothing. Direct remove still throws the strict exception. A missing queue still raises the required error.

This extension is independent of the required dispatch implementation and has its own checks. Its saved project remains separate from the required attempt.

## JDK21 commands

Run from the chosen starter or solution folder containing sources.txt. No external libraries or standard input are needed.

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out Main baseline
    java -ea -cp out ContractChecks baseline
    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core
    java -ea -cp out Main core

Optional:

    java -ea -cp out dispatch.optional.OptionalChecks baseline
    java -ea -cp out dispatch.optional.OptionalChecks full
    java -da -cp out dispatch.optional.OptionalChecks full

The untouched starter compiles and passes both baseline modes. Required core and optional full fail explicitly until their TODOs are implemented. Explicit checks stay active with assertions disabled. Record each summary and compare both six-line client outputs.

## Completion evidence

Keep the unchanged baseline and source client; retain commands, results and REFACTORING.md. Show the two-query/no-write case, two sends, prior snapshot, changed configuration with preserved history, duplicate-label lookup and each required error boundary. Explain the three modified required methods and one counterexample that would expose an accidental extra write. For the optional task, retain FIFO outputs, repeated-empty behavior, strict remove failure and required-null failure.
