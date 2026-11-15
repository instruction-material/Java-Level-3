# Refactoring Clinic 12: Replace conditional branches with runtime variants

The starter's original public behavior and Main core work. The post-refactor core check reports 'Complete rule registry' until the new required design entry points are completed. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    text=go

## Diagnose and migrate one branch

MessageService currently owns a conditional for plain and compact output. Trace both modes with message "go": plain returns "text=go\n" and compact returns "2|go\n". The newline is part of each result. Empty input is valid: compact returns "0|\n". Message length is Java String.length, so it counts UTF-16 char units, not displayed glyphs.

Extract PlainRule first and route only the plain branch through it. Keep compact on the existing path, compile, and run baseline. Then extract CompactRule. Move selection to an assembly boundary with a defensive copy of a rule registry; the rendering method validates and dispatches through RenderRule. Adding a new caller-supplied variant should leave that method unchanged. A small selection map at assembly is compatible with polymorphic behavior.

Required API: new MessageService() still supports exactly plain and compact. Add the already-scaffolded MessageService(Map<String,RenderRule>) so callers can supply a different registry. Validate null registry with NullPointerException("rules"). Validate entries in the supplied map's iteration order: keys must match [a-z]{1,12}; null key throws NullPointerException("mode"), invalid key IllegalArgumentException("mode"), and null rule NullPointerException("rule"). Snapshot the mapping so later caller mutations do not change dispatch. An empty registry is valid and supports no modes.

For render, validate mode non-null first (message "mode"), then message. A null message throws NullPointerException("message"). Strings longer than 24 UTF-16 char units, or containing pipe, carriage return or newline, throw IllegalArgumentException("message"). Empty strings, accepted spaces and Unicode are retained exactly. Finally reject an unavailable mode with IllegalArgumentException("mode"). Invoke only the selected rule, exactly once. Rule results and runtime failures pass through this core boundary.

PlainRule returns "text="+message+"\n"; CompactRule returns message.length()+"|"+message+"\n". Their input is validated by the facade. The reference uses ordinary objects and a map; inheritance is unnecessary for these independent policies. Compare this with a switch when the variant set is small and fixed. The refactor is justified here by a caller-supplied rule requirement and preserved branch-specific behavior.

Record the original branch hotspot, the actual plain-only migration, both protected outputs, the registry's owner, an unsupported-mode result and a new caps rule. Explain which conditional remains in assembly and why new behavior resides in the rules.

## Independent transfer: validate and render a batch

Complete RenderBatch.lines(Messages renderer,String mode,List<String> lines). Validate renderer, mode and the list in that order with NullPointerException messages "renderer", "mode" and "lines". Copy the input and validate every message using the same text rules before invoking any renderer. Null items report "message". Render each item once in original order and concatenate results exactly. Retain the caller's list.

An empty list returns "" without dispatch, so an unavailable mode is observed only when an item is actually rendered. A runtime failure propagates and stops later calls. Earlier callback effects have no rollback promise.

A recorder that returns mode+":"+text+"\n" over ["a", "", " b "] produces "plain:a\nplain:\nplain: b \n". Retain its call log, a later invalid item with zero renderer calls, and a deliberately failing callback. This task can finish with the core registry still unfinished.

## Independent extension: compose a prefix policy

Complete PrefixRule implements RenderRule. The constructor validates prefix first: null throws NullPointerException("prefix"); more than 12 UTF-16 char units or any carriage return/newline throws IllegalArgumentException("prefix"). Other prefix text, including an empty prefix, is accepted exactly. Then validate delegate with NullPointerException("delegate").

render validates message using the core text rules, calls the delegate once, rejects a null returned result with NullPointerException("output"), and returns prefix followed by that exact result. Runtime failures propagate. Prefixes A then B around an identity rule yield "ABgo", not "goBA". This adds a new composable policy separately from preserving the two original outputs.

Retain exact prefix ordering, one delegate-call observation, invalid input with no call, a null-result failure and a nested policy. Compare implementing a new rule with editing the old conditional.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import app.MessageService;
    import api.RenderRule;
    import java.util.*;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            RenderRule caps=text->text.toUpperCase(Locale.ROOT);
            MessageService messages=new MessageService(Map.of("caps",caps));
            if(!messages.render("caps","go").equals("GO"))throw new AssertionError("variant");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
