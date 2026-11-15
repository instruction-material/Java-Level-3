# Clinic 17 review record

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

## Plan a sequence rather than a rewrite

The working BoardReport repeats unfinished-minute calculations and mixes calculation with report formatting. These are two concrete smells with different likely change causes. Trace the public summary and remainingMinutes calls before editing and write the proposed sequence in REFACTORING.md.

Tasks are immutable. Title is validated first: null throws NullPointerException("title"); blank, longer than 16 UTF-16 char units, or containing comma, pipe, carriage return or newline throws IllegalArgumentException("title"). Minutes must be 0..120, otherwise IllegalArgumentException("minutes"). Duplicate titles and zero minutes are valid.

Both board entry points validate a non-null list with NullPointerException("tasks"), then at most eight tasks with IllegalArgumentException("tasks"), then non-null members with NullPointerException("task"). Preserve caller data and encounter order. remainingMinutes sums only tasks whose done flag is false.

summary returns exactly four lines: "tasks=N\n", "done=D\n", "remaining=M\n" and "open=TITLES\n". TITLES are unfinished titles in encounter order, comma-separated without added spaces; "-" represents none. Retain duplicate titles. The sample [read(10,false),code(20,true),draw(5,false)] produces tasks=3, done=1, remaining=15 and open=read,draw. Empty input gives zero counts and open=-.

Use these checkpoints:

1. Extract the duplicated unfinished-minute loop into one private method. Run baseline and keep this small passing state.
2. Move analysis into an owned BoardAnalysis while formatting stays in BoardReport. Validate the input once at that ownership boundary and produce a BoardStats value. Run baseline again and decide whether the planned next step is still safe.
3. Move only formatting to BoardText, retaining the public facade and exact output. Run core and the unchanged external client.

BoardStats is a frozen snapshot: tasks 0..8, done 0..tasks, remaining 0..960, and an unmodifiable copied openTitles list with size tasks-done. Its constructor validates those fields in that order and uses their field names in errors. Null members are rejected by List.copyOf. Derived stats retain unfinished titles in original order.

The reference performs one analysis pass, stores immutable state and lets a renderer consume the resulting value. Class counts alone do not show improvement: identify the responsibility moved and a change now confined behind that boundary.

Retain the actual source/results for both intermediate checkpoints and the final design. Replay a retained earlier source in an isolated working copy to demonstrate a recoverable checkpoint. Keep personal work intact. Name a first proposed step that would be too broad and split it. Sorting titles changes observable output, so postpone it from the behavior-preserving core and evaluate it separately.

This final reference illustrates the described responsibility split. Retain the actual source checkpoints used in an attempt; passing behavior alone does not establish that sequence or its design judgment.
