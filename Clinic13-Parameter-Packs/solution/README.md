# Refactoring Clinic 13: Introduce a coherent parameter object

The starter's original public behavior and Main core work. The post-refactor core check reports 'Complete parameter object' until the new required design entry points are completed. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    2..9/3:[2, 5, 8]

## Trace the data clump

The old Ranges API passes start, end and step separately to values and describe. For (2,9,3), values are [2,5,8] and describe is "2..9/3:[2, 5, 8]". End is inclusive only when reached by a whole step; 9 is not automatically appended. Equal endpoints produce one value.

All three integers form one coherent range concept. Introduce the scaffolded RangeSpec record as its validation boundary, then make new values(RangeSpec) and describe(RangeSpec) methods consume the whole object. Keep the old integer signatures as adapters so existing clients preserve their behavior. A parameter object groups related meaning; unrelated output formatting preferences need not join it.

Validation order is start, end, step, then range consistency. Start and end must each be 0 through 100; violations throw IllegalArgumentException("start") or ("end"). Step must be 1 through 20; violations report "step". Only after those checks, end<start throws IllegalArgumentException("range"). Thus (10,0,0) reports step; (10,0,1) reports range. RangeSpec construction and the old API share those promises.

Return an unmodifiable ascending list starting at start and repeatedly adding step while the value is <=end. The new overloads reject null RangeSpec with NullPointerException("range"). describe preserves the exact "START..END/STEP:[list]" format, with ordinary List spacing and no newline in the returned string. Main adds a newline.

First complete RangeSpec while leaving all original call sites untouched and rerun baseline. Then migrate enumeration to the object overload, adapt the old integer method and migrate describe. Compare old and new calls on a nonlanding end and an invalid group. The reference delegates old entry points to a validated RangeSpec.

Record the three values' path through both old methods, the new validation boundary, changed call sites, equal/nonlanding endpoint cases, an invalid group and a judgment about whether the object clarified the API. Passing old behavior alone does not prove the new whole-object entry points work.

## Independent transfer: preserve a whole list

Complete EveryOther.select(List<Integer> values). Null input throws NullPointerException("values"); null members are rejected by List.copyOf. Return an unmodifiable list of positions 0,2,4,..., retaining values, duplicates and original relative order. Accept arbitrary integers, including negatives. Keep the caller's list unchanged.

For [8,1,8,2,9], the result is [8,8,9]. Empty input produces an empty list and a one-element input retains that element. This is index selection, not choosing even-valued integers. Discuss how passing the whole immutable input avoids a separate size parameter that could disagree with the list.

Retain an odd-length case, duplicate values, an empty list and an input-purity check. This task is independent of RangeSpec.

## Independent extension: combine two samples

Complete MergeRanges.unique(first,second). Validate and snapshot first, then second, with NullPointerException("first") or ("second") for null lists. Null members are rejected before merging. Accept arbitrary int values in either order, remove duplicates across both inputs and return an unmodifiable ascending list. Preserve both caller inputs.

[8,2,5,2] and [9,1,5] produce [1,2,5,8,9]. Two empty inputs produce []. Comparison must remain correct for Integer.MIN_VALUE and MAX_VALUE; subtraction is an unsafe comparator for that domain. The reference uses natural ordering in a TreeSet.

This adds a separate combination feature rather than changing the old range's inclusive-step policy. Retain overlapping/empty/extreme cases and both unchanged inputs.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import app.*;
    import java.util.List;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            RangeSpec range=new RangeSpec(2,9,3);
            RangeService service=new RangeService();
            if(!service.values(range).equals(List.of(2,5,8)))throw new AssertionError("whole object");
            if(!service.values(2,9,3).equals(service.values(range)))throw new AssertionError("old adapter");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
