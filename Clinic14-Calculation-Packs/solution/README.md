# Refactoring Clinic 14: Split temporaries and extract calculation queries

The starter's original public behavior and Main core work. The post-refactor core check reports 'Complete named queries' until the new required design entry points are completed. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    EnergyResult[raw=99, usable=49, lost=50]

## Split a reused temporary before extracting queries

The working measure(watts,seconds,efficiency) reuses a temporary first for raw energy and then for usable energy. Its input is constant power in watts, a duration in seconds, and an integer efficiency percentage. This simplified calculation uses raw=watts*seconds joules, usable=floor(raw*efficiency/100), and lost=raw-usable. It models the stated arithmetic, not a physical measurement or variable-power simulation.

Validation order is watts (0..1000), seconds (0..3600), then efficiency (0..100), inclusive. Invalid values throw IllegalArgumentException with message "watts", "seconds" or "efficiency". Each EnergyResult is immutable; raw is 0..3600000, usable is 0..raw and lost must equal raw-usable. Its constructor validates those fields in that order, reporting "raw", "usable" or "lost".

For (33,3,50), raw is 99, usable is 49 and lost is 50. Multiplying before integer division is essential: 99/100*50 gives the wrong usable value. For (1000,3600,100), raw and usable are 3600000 and lost is zero. Zero power, duration or efficiency follows the same formula.

First name rawJoules and usableJoules separately while keeping all arithmetic and validation order. Run baseline and record those two values. Then complete the public raw(watts,seconds) and usable(raw,efficiency) query scaffolds and call them from measure. raw validates watts then seconds and returns their product. usable validates raw in 0..3600000 with IllegalArgumentException("raw"), then efficiency, and returns raw*efficiency/100. The stated bounds keep this product within int.

The reference gives each query one meaning and no state changes. Query extraction is appropriate because the computation is repeatable and has no outside effects. A temporary that holds a one-time random, file or network result cannot simply be replaced by a repeatedly executed query.

Record the reused variable's meanings, the split-variable checkpoint, exact intermediate values, the extracted query boundaries and the rounding-sensitive counterexample. Retain an invalid input and a maximum input. Explain why changing rounding policy would be new behavior.

## Independent transfer: quantify loss

Complete EnergyLoss.percentage(EnergyResult). Null input throws NullPointerException("result"). Return zero when raw is zero; otherwise return floor(lost*100/raw). Do not modify the result. Widen the numerator before multiplying when adapting the domain; the reference uses long.

For (99,49,50), the returned percentage is 50. It is a percentage of raw energy, distinct from the usable count or its percentage. Retain zero energy, all lost, no loss and a fractional result.

## Independent extension: aggregate a bounded batch

Complete EnergyBatch.total(List<EnergyResult>). Null list throws NullPointerException("readings"). Snapshot the whole list; null members are rejected. After the snapshot, more than 20 readings throws IllegalArgumentException("readings"). Sum raw, usable and lost separately and return immutable BatchTotal with long fields. Preserve all input entries and order.

An empty batch gives (0,0,0). (99,49,50) plus (100,100,0) gives (199,149,50). Twenty maximum readings can total raw=72000000; this aggregate uses its own type because that exceeds one measurement's bound. BatchTotal validates raw 0..72000000, usable 0..raw and lost=raw-usable in that order.

Retain two different readings, empty and maximum-size batches, a rejected over-size batch and the original input. Aggregating measurements is separate from changing the single-reading formula.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import app.EnergyMeter;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            EnergyMeter meter=new EnergyMeter();
            int raw=meter.raw(33,3),usable=meter.usable(raw,50);
            if(raw!=99||usable!=49)throw new AssertionError("intermediate values");
            if(meter.measure(33,3,50).lost()!=50)throw new AssertionError("loss");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
