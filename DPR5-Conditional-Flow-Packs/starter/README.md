# DPR5: Simplifying conditional expressions

Refactor a lab-booking decision while preserving its result and observable work. The nested baseline runs before any learner implementation. Admission is required; fee variants and an optional status reporter form a separate extension.

## Inputs and outputs

`Request` has `machine`, `member`, `minutes`, `closed`, `supervised` and `busy`.
A present `Member` has `suspended`, `revoked` and `trainingLevel`. A missing member represents a guest. Minutes are whole numbers from 1 through 240; present members have training levels 0 through 5.

| Machine | Required training | Maximum booking minutes |
| --- | ---: | ---: |
| STANDARD | 0 | 120 |
| LASER | 2 | 60 |
| CNC | 3 | 45 |

`Decision(allowed, reason, grantedMinutes)` grants the requested minutes when approved and zero when denied. The supplied caller `BookingClient.describe` returns `reason:minutes`; preserve that signature and output.

`AuditTrail` belongs to the caller. Preserve existing history and append the current evaluation. `events()` returns an immutable snapshot.

## Validation and first-match decisions

Call `Inputs.validate` before inspecting eligibility or writing events. It enforces this order:

1. Missing audit: `NullPointerException("audit")`.
2. Missing request: `IllegalArgumentException("request")`.
3. Missing machine: `IllegalArgumentException("machine")`.
4. Minutes outside 1–240: `IllegalArgumentException("minutes")`.
5. A present member's training outside 0–5: `IllegalArgumentException("trainingLevel")`.

When several arguments are invalid, the first check determines the exception. Invalid training remains invalid when the account is suspended or the lab is closed. Validation failure leaves existing audit history unchanged.

For a valid request, the first matching row wins:

| Condition | Reason | Allowed |
| --- | --- | --- |
| Member absent | `guest` | false |
| Suspended or revoked | `inactive` | false |
| Lab closed | `closed` | false |
| Duration exceeds this machine's limit | `too-long` | false |
| Neither supervised nor sufficiently trained | `training` | false |
| Machine busy | `busy` | false |
| No earlier condition | `approved` | true |

These are fictional exercise rules. `LegacyGate.java` preserves their nested baseline.

The training probe records `training:actual>=required` and returns that comparison. Supervision skips it. An unsupervised STANDARD booking still records training even though every valid level passes.
Availability records `availability:busy=true` or `availability:busy=false` and returns the opposite of `busy`. Probe it only after readiness succeeds.
Every valid evaluation appends exactly one final `decision:reason` event after its probes. An earlier denial prevents later probes.

Worked observations:

- LASER, active level 2,60 minutes, open, unsupervised, not busy: `approved:60` with `[training:2>=2, availability:busy=false, decision:approved]`.
- CNC, active level 0,20 minutes, open, supervised, busy: `busy:0` with `[availability:busy=true, decision:busy]`.
- LASER, active level 1,60 minutes, open, unsupervised, busy: `training:0` with `[training:1>=2, decision:training]`. Availability is skipped.
- Guest, closed, busy,240 valid minutes: `guest:0` with `[decision:guest]`. A missing machine would fail validation first.

## Required project

Implement the three marked methods in `src/lab/GatePolicy.java`:

1. Decompose inactive-account and readiness conditions into `blocked` and `ready`.
2. Replace nested flags with ordered guards. Consolidate the equivalent suspended/revoked denial.
3. Use `finish` so each normal exit records one decision and preserves caller history.

Predict outcomes before editing; record comparisons in `REFACTORING.md`. Keep the supplied baseline and explicit checks unchanged. Delegating to `LegacyGate` does not demonstrate the assigned refactoring. The required implementation compiles independently of optional sources, demos, tests and the baseline.

## Optional extension

Implement the marked methods in `optional/FeePolicies.java` and `optional/OptionalWorkflow.java`.
The supplied factory selects a `Policy` variant. Put each algorithm in that variant; the bounded factory dispatch remains at the composition point.

| Machine | Credits for valid duration m | Boundary examples |
| --- | --- | --- |
| STANDARD | `ceil(m / 30) * 4` | 1→4,30→4,31→8 |
| LASER | `6 + 2*m` | 1→8,60→126 |
| CNC | `9 + ceil(m / 15) * 7` | 1→16,15→16,16→23 |

Fee methods accept all durations 1–240 independently of admission limits. The factory rejects a missing machine with `IllegalArgumentException("machine")`. Each fee method rejects invalid duration with `IllegalArgumentException("minutes")`. `LegacyOptionalWorkflow` supplies the original fee/reporter behavior.

The workflow uses the refactored policy and returns `Outcome(decision, credits)`. Compute fees only for approvals; denials cost zero.
A supplied reporter receives `started` before admission, followed by `completed(decision, credits)` or `failed(error)` before the original runtime exception is rethrown. Reporter callbacks in this exercise complete normally.
Normalize an absent reporter once to `StatusReporter.none()`. Make ordinary calls through it. The observer is optional; the audit remains required and guests remain denied. Preserve the core audit.

Worked workflow: the approved LASER 60 case costs 126 credits. A recording reporter sees `started` then `completed:approved:126`. An absent reporter changes neither result nor audit. A missing request reports `started` then `failed:IllegalArgumentException:request`; existing audit history remains unchanged.

## JDK 21 commands

Run from the selected role folder containing `sources.txt`. The argument file explicitly lists all 15 Java sources; the compiler creates `out`.

```sh
javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out "@sources.txt"
java -ea -cp out lab.CoreDemo baseline
java -ea -cp out lab.CoreChecks baseline
java -ea -cp out lab.optional.OptionalChecks baseline
```

Run Java only after compilation succeeds. Success has exit status 0; inspect `$?` in a POSIX shell or `$LASTEXITCODE` in PowerShell.
The untouched starter compiles and baseline modes pass. Its unfinished core/optional modes intentionally fail with a TODO exception.

After completing the core, recompile and run:

```sh
java -ea -cp out lab.CoreDemo core
java -ea -cp out lab.CoreChecks core
java -da -cp out lab.CoreChecks core
```

Both core demo modes print:

```text
approved:60
[training:2>=2, availability:busy=false, decision:approved]
busy:0
[availability:busy=true, decision:busy]
```

Core checks cover all 144,000 valid combinations:3 machines ×25 member states (guest plus 24 flag/training combinations) ×240 durations ×8 lab/supervision/busy states. They verify decisions, exact events, stable caller output, validation precedence, repeated history and immutable snapshots. This is exhaustive for that finite model.

After completing the optional extension, recompile and run:

```sh
java -ea -cp out lab.optional.OptionalDemo
java -ea -cp out lab.optional.OptionalChecks full
```

The demo prints `approved credits=126`. Optional checks cover all 720 machine/duration pairs and 720 workflow cases, including present/absent reporters, boundaries, denials and failure ordering.

## Assertions and mandatory validation

`BookingClient` asserts internally that denied decisions grant zero minutes. Compare:

```sh
java -ea -cp out lab.CoreChecks assertions
java -da -cp out lab.CoreChecks assertions
```

The deliberately bad policy triggers that assertion with `-ea`; with `-da` the demonstration observes its bad value. Both demonstrations also verify ordinary public validation remains active. Summaries are `assertions=on public-validation=active` and `assertions=off public-validation=active`.

Java assertions can be disabled. Enforce required public arguments with ordinary checks. The supplied contract runner uses explicit checks that remain active in either mode.

## Completion evidence

Required: explain first-match priority, overlapping denials, one skipped probe, one probe that must remain, validation precedence and reused history. Record actual commands and outcomes with assertions enabled and disabled. Explain how reordering a side-effecting condition or duplicating a final action changes behavior.

Optional: record fee boundaries, both reporter choices and required-audit failure. Identify the remaining dispatch conditional and explain why an absent observer suits a Null Object.

## References

- [Simplifying conditional expressions](https://refactoring.guru/refactoring/techniques/simplifying-conditional-expressions): technique names; this lab and its data are original course material.
- [JDK 21 javac](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javac.html): release targeting, argument files and output directories.
- [Oracle assertion guidance](https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html): internal invariants and public argument validation.
