# Clinic 12 review record

## Before changing source
Public call and exact input/output:
Source locations and concrete smell:
Behavior, validation order and data that must stay stable:
Chosen boundary and why it fits the problem:

## Actual small steps
Retain each source checkpoint or commit identity.
For each step: prediction, change, passing output and next risk.
First extraction:
Next responsibility move:
Final client and boundary checks:
Recoverable checkpoint or postponed change:

## Review
Before/after responsibility sketch:
One observed benefit and one tradeoff:
Standard, boundary and failure cases:
One tempting change that would alter behavior:
Pattern or technique justified by this case:

## Optional independent work
Transfer predictions, actual output and retained inputs:
Extension predictions, actual output and retained inputs:
What these observations establish and what needs further review:

## Reference notes

## Diagnose and migrate one branch

MessageService currently owns a conditional for plain and compact output. Trace both modes with message "go": plain returns "text=go\n" and compact returns "2|go\n". The newline is part of each result. Empty input is valid: compact returns "0|\n". Message length is Java String.length, so it counts UTF-16 char units, not displayed glyphs.

Extract PlainRule first and route only the plain branch through it. Keep compact on the existing path, compile, and run baseline. Then extract CompactRule. Move selection to an assembly boundary with a defensive copy of a rule registry; the rendering method validates and dispatches through RenderRule. Adding a new caller-supplied variant should leave that method unchanged. A small selection map at assembly is compatible with polymorphic behavior.

Required API: new MessageService() still supports exactly plain and compact. Add the already-scaffolded MessageService(Map<String,RenderRule>) so callers can supply a different registry. Validate null registry with NullPointerException("rules"). Validate entries in the supplied map's iteration order: keys must match [a-z]{1,12}; null key throws NullPointerException("mode"), invalid key IllegalArgumentException("mode"), and null rule NullPointerException("rule"). Snapshot the mapping so later caller mutations do not change dispatch. An empty registry is valid and supports no modes.

For render, validate mode non-null first (message "mode"), then message. A null message throws NullPointerException("message"). Strings longer than 24 UTF-16 char units, or containing pipe, carriage return or newline, throw IllegalArgumentException("message"). Empty strings, accepted spaces and Unicode are retained exactly. Finally reject an unavailable mode with IllegalArgumentException("mode"). Invoke only the selected rule, exactly once. Rule results and runtime failures pass through this core boundary.

PlainRule returns "text="+message+"\n"; CompactRule returns message.length()+"|"+message+"\n". Their input is validated by the facade. The reference uses ordinary objects and a map; inheritance is unnecessary for these independent policies. Compare this with a switch when the variant set is small and fixed. The refactor is justified here by a caller-supplied rule requirement and preserved branch-specific behavior.

Record the original branch hotspot, the actual plain-only migration, both protected outputs, the registry's owner, an unsupported-mode result and a new caps rule. Explain which conditional remains in assembly and why new behavior resides in the rules.

This final reference illustrates the described responsibility split. Retain the actual source checkpoints used in an attempt; passing behavior alone does not establish that sequence or its design judgment.
